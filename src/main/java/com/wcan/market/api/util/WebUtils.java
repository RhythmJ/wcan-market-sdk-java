package com.wcan.market.api.util;

import com.wcan.market.api.FileItem;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 网络请求工具类
 *
 * @author majunjie
 * @date 2019/4/17 16:13
 */
public abstract class WebUtils {

    public static final String DEFAULT_CHARSET = "UTF-8";
    private static boolean ignoreSSLCheck = true;
    private static boolean ignoreHostCheck = true;

    private WebUtils() {
    }

    public static void setIgnoreSSLCheck(boolean ignoreSSLCheck) {
        ignoreSSLCheck = ignoreSSLCheck;
    }

    public static void setIgnoreHostCheck(boolean ignoreHostCheck) {
        ignoreHostCheck = ignoreHostCheck;
    }

    public static String doPost(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws IOException {
        return doPost(url, params, "UTF-8", connectTimeout, readTimeout);
    }

    public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int readTimeout) throws IOException {
        return doPost(url, (Map)params, (String)charset, connectTimeout, readTimeout, (Map)null, (Proxy)null);
    }

    public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int readTimeout, Map<String, String> headerMap, Proxy proxy) throws IOException {
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        byte[] content = new byte[0];
        if (query != null) {
            content = query.getBytes(charset);
        }

        return _doPost(url, ctype, content, connectTimeout, readTimeout, headerMap, proxy);
    }

    public static String doPost(String url, String apiBody, String contentType, String charset, int connectTimeout, int readTimeout) throws IOException {
        String ctype;
        if (StringUtils.isBlank(contentType)) {
            ctype = "text/plain;charset=" + charset;
        } else {
            ctype =  contentType + ";charset=" + charset;
        }
        byte[] content = apiBody.getBytes(charset);
        return _doPost(url, ctype, content, connectTimeout, readTimeout, null, (Proxy)null);
    }

    public static String doPost(String url, String apiBody, String charset, int connectTimeout, int readTimeout, Map<String, String> headerMap) throws IOException {
        String ctype = "text/plain;charset=" + charset;
        byte[] content = apiBody.getBytes(charset);
        return _doPost(url, ctype, content, connectTimeout, readTimeout, headerMap, (Proxy)null);
    }

    public static String doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout) throws IOException {
        return _doPost(url, ctype, content, connectTimeout, readTimeout, (Map)null, (Proxy)null);
    }

    public static String doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout, Map<String, String> headerMap, Proxy proxy) throws IOException {
        return _doPost(url, ctype, content, connectTimeout, readTimeout, headerMap, proxy);
    }

    private static String _doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout, Map<String, String> headerMap, Proxy proxy) throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;

        try {
            conn = getConnection(new URL(url), "POST", ctype, headerMap, proxy);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            out = conn.getOutputStream();
            out.write(content);
            rsp = getResponseAsString(conn);
        } finally {
            if (out != null) {
                out.close();
            }

            if (conn != null) {
                conn.disconnect();
            }

        }

        return rsp;
    }

    public static String doPost(String url, Map<String, String> params, Map<String, FileItem> fileParams, int connectTimeout, int readTimeout) throws IOException {
        return fileParams != null && !fileParams.isEmpty() ? doPost(url, params, fileParams, "UTF-8", connectTimeout, readTimeout) : doPost(url, params, "UTF-8", connectTimeout, readTimeout);
    }

    public static String doPost(String url, Map<String, String> params, Map<String, FileItem> fileParams, String charset, int connectTimeout, int readTimeout) throws IOException {
        return doPost(url, params, fileParams, charset, connectTimeout, readTimeout, (Map)null);
    }

    public static String doPost(String url, Map<String, String> params, Map<String, FileItem> fileParams, String charset, int connectTimeout, int readTimeout, Map<String, String> headerMap) throws IOException {
        return fileParams != null && !fileParams.isEmpty() ? _doPostWithFile(url, params, fileParams, charset, connectTimeout, readTimeout, headerMap) : doPost(url, (Map)params, (String)charset, connectTimeout, readTimeout, headerMap, (Proxy)null);
    }

    private static String _doPostWithFile(String url, Map<String, String> params, Map<String, FileItem> fileParams, String charset, int connectTimeout, int readTimeout, Map<String, String> headerMap) throws IOException {
        String boundary = String.valueOf(System.nanoTime());
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;

        try {
            String ctype = "multipart/form-data;charset=" + charset + ";boundary=" + boundary;
            conn = getConnection(new URL(url), "POST", ctype, headerMap, (Proxy)null);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            out = conn.getOutputStream();
            byte[] entryBoundaryBytes = ("\r\n--" + boundary + "\r\n").getBytes(charset);
            Set<Entry<String, String>> textEntrySet = params.entrySet();
            Iterator i$ = textEntrySet.iterator();

            while(i$.hasNext()) {
                Entry<String, String> textEntry = (Entry)i$.next();
                byte[] textBytes = getTextEntry((String)textEntry.getKey(), (String)textEntry.getValue(), charset);
                out.write(entryBoundaryBytes);
                out.write(textBytes);
            }

            Set<Entry<String, FileItem>> fileEntrySet = fileParams.entrySet();
            i$ = fileEntrySet.iterator();

            while(i$.hasNext()) {
                Entry<String, FileItem> fileEntry = (Entry)i$.next();
                FileItem fileItem = (FileItem)fileEntry.getValue();
                if (!fileItem.isValid()) {
                    throw new IOException("FileItem is invalid");
                }

                byte[] fileBytes = getFileEntry((String)fileEntry.getKey(), fileItem.getFileName(), fileItem.getMimeType(), charset);
                out.write(entryBoundaryBytes);
                out.write(fileBytes);
                fileItem.write(out);
            }

            byte[] endBoundaryBytes = ("\r\n--" + boundary + "--\r\n").getBytes(charset);
            out.write(endBoundaryBytes);
            rsp = getResponseAsString(conn);
            return rsp;
        } finally {
            if (out != null) {
                out.close();
            }

            if (conn != null) {
                conn.disconnect();
            }

        }
    }

    private static byte[] getTextEntry(String fieldName, String fieldValue, String charset) throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(fieldName);
        entry.append("\"\r\nContent-Type:text/plain\r\n\r\n");
        entry.append(fieldValue);
        return entry.toString().getBytes(charset);
    }

    private static byte[] getFileEntry(String fieldName, String fileName, String mimeType, String charset) throws IOException {
        StringBuilder entry = new StringBuilder();
        entry.append("Content-Disposition:form-data;name=\"");
        entry.append(fieldName);
        entry.append("\";filename=\"");
        entry.append(fileName);
        entry.append("\"\r\nContent-Type:");
        entry.append(mimeType);
        entry.append("\r\n\r\n");
        return entry.toString().getBytes(charset);
    }

    public static String doGet(String url, Map<String, String> params) throws IOException {
        return doGet(url, params, "UTF-8");
    }

    public static String doGet(String url, Map<String, String> params, String charset) throws IOException {
        HttpURLConnection conn = null;
        String rsp = null;

        try {
            String ctype = "application/x-www-form-urlencoded;charset=" + charset;
            String query = buildQuery(params, charset);
            conn = getConnection(buildGetUrl(url, query), "GET", ctype, (Map)null, (Proxy)null);
            rsp = getResponseAsString(conn);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

        return rsp;
    }

    private static HttpURLConnection getConnection(URL url, String method, String ctype, Map<String, String> headerMap, Proxy proxy) throws IOException {
        HttpURLConnection conn = null;
        if (proxy == null) {
            conn = (HttpURLConnection)url.openConnection();
        } else {
            conn = (HttpURLConnection)url.openConnection(proxy);
        }

        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection connHttps = (HttpsURLConnection)conn;
            if (ignoreSSLCheck) {
                try {
                    SSLContext ctx = SSLContext.getInstance("TLS");
                    ctx.init((KeyManager[])null, new TrustManager[]{new WebUtils.TrustAllTrustManager()}, new SecureRandom());
                    connHttps.setSSLSocketFactory(ctx.getSocketFactory());
                    connHttps.setHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                } catch (Exception var8) {
                    throw new IOException(var8.toString());
                }
            } else if (ignoreHostCheck) {
                connHttps.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }

            conn = connHttps;
        }

        ((HttpURLConnection)conn).setRequestMethod(method);
        ((HttpURLConnection)conn).setDoInput(true);
        ((HttpURLConnection)conn).setDoOutput(true);
        if (headerMap != null && headerMap.get("TOP_HTTP_DNS_HOST") != null) {
            ((HttpURLConnection)conn).setRequestProperty("Host", (String)headerMap.get("TOP_HTTP_DNS_HOST"));
        } else {
            ((HttpURLConnection)conn).setRequestProperty("Host", url.getHost());
        }

        ((HttpURLConnection)conn).setRequestProperty("Accept", "*");
        ((HttpURLConnection)conn).setRequestProperty("User-Agent", "top-sdk-java");
        ((HttpURLConnection)conn).setRequestProperty("Content-Type", ctype);
        if (headerMap != null) {
            Iterator i$ = headerMap.entrySet().iterator();

            while(i$.hasNext()) {
                Entry<String, String> entry = (Entry)i$.next();
                if (!"TOP_HTTP_DNS_HOST".equals(entry.getKey())) {
                    ((HttpURLConnection)conn).setRequestProperty((String)entry.getKey(), (String)entry.getValue());
                }
            }
        }

        return (HttpURLConnection)conn;
    }

    private static URL buildGetUrl(String url, String query) throws IOException {
        return StringUtils.isEmpty(query) ? new URL(url) : new URL(buildRequestUrl(url, query));
    }

    public static String buildRequestUrl(String url, String... queries) {
        if (queries != null && queries.length != 0) {
            StringBuilder newUrl = new StringBuilder(url);
            boolean hasQuery = url.contains("?");
            boolean hasPrepend = url.endsWith("?") || url.endsWith("&");
            String[] arr$ = queries;
            int len$ = queries.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String query = arr$[i$];
                if (!StringUtils.isEmpty(query)) {
                    if (!hasPrepend) {
                        if (hasQuery) {
                            newUrl.append("&");
                        } else {
                            newUrl.append("?");
                            hasQuery = true;
                        }
                    }

                    newUrl.append(query);
                    hasPrepend = false;
                }
            }

            return newUrl.toString();
        } else {
            return url;
        }
    }

    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params != null && !params.isEmpty()) {
            StringBuilder query = new StringBuilder();
            Set<Entry<String, String>> entries = params.entrySet();
            boolean hasParam = false;
            Iterator i$ = entries.iterator();

            while(i$.hasNext()) {
                Entry<String, String> entry = (Entry)i$.next();
                String name = (String)entry.getKey();
                String value = (String)entry.getValue();
                if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(name)) {
                    if (hasParam) {
                        query.append("&");
                    } else {
                        hasParam = true;
                    }
                    query.append(name).append("=").append(URLEncoder.encode(value, charset));
                }
            }

            return query.toString();
        } else {
            return null;
        }
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        if (conn.getResponseCode() < 400) {
            String contentEncoding = conn.getContentEncoding();
            return "gzip".equalsIgnoreCase(contentEncoding) ? getStreamAsString(new GZIPInputStream(conn.getInputStream()), charset) : getStreamAsString(conn.getInputStream(), charset);
        } else {
            if (conn.getResponseCode() == 400) {
                InputStream error = conn.getErrorStream();
                if (error != null) {
                    return getStreamAsString(error, charset);
                }
            }

            throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage());
        }
    }

    public static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);
            StringBuilder response = new StringBuilder();
            char[] buff = new char[1024];
            boolean var5 = false;

            int read;
            while((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }

            String var6 = response.toString();
            return var6;
        } finally {
            if (stream != null) {
                stream.close();
            }

        }
    }

    public static String getResponseCharset(String ctype) {
        String charset = "UTF-8";
        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            String[] arr$ = params;
            int len$ = params.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String param = arr$[i$];
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2 && !StringUtils.isEmpty(pair[1])) {
                        charset = pair[1].trim();
                    }
                    break;
                }
            }
        }

        return charset;
    }

    public static String decode(String value) {
        return decode(value, "UTF-8");
    }

    public static String encode(String value) {
        return encode(value, "UTF-8");
    }

    public static String decode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        }

        return result;
    }

    public static String encode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        }

        return result;
    }

    public static Map<String, String> splitUrlQuery(String query) {
        Map<String, String> result = new HashMap();
        String[] pairs = query.split("&");
        if (pairs != null && pairs.length > 0) {
            String[] arr$ = pairs;
            int len$ = pairs.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String pair = arr$[i$];
                String[] param = pair.split("=", 2);
                if (param != null && param.length == 2) {
                    result.put(param[0], param[1]);
                }
            }
        }

        return result;
    }

    public static class TrustAllTrustManager implements X509TrustManager {
        public TrustAllTrustManager() {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }
}
