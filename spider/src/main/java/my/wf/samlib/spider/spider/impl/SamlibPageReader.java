package my.wf.samlib.spider.spider.impl;

import my.wf.samlib.spider.error.BaseException;
import my.wf.samlib.spider.spider.PageReader;
import my.wf.samlib.spider.spider.error.SpiderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SamlibPageReader implements PageReader {

    public static final String LINK_SUFFIX = "indextitle.shtml";
    private static final String DEFAULT_ENCODING = "windows-1251";
    private static final Logger logger = LoggerFactory.getLogger(SamlibPageParser.class);
    private static final Pattern charsetPattern = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");

    private String getCharset(String contentType, String encoding) {
        if (null == contentType) {
            return encoding;
        }
        Matcher m = charsetPattern.matcher(contentType);
        return m.matches() ? m.group(1) : encoding;
    }

    @Override
    public String readPageByUrl(String url) {
        logger.debug("read page [{}]", url);
        String pageString;
        try {
            pageString = readPageByLink(url);
        } catch (IOException e) {
            throw new SpiderException(url, "can't read link", e);
        }
        logger.debug("downloaded {} symbols", pageString.length());
        return pageString;
    }

    private String readPageByLink(String link) throws IOException {
        URL url = new URL(link);
        URLConnection con = url.openConnection();
        StringBuilder buf = new StringBuilder();
        try (Reader r = new InputStreamReader(con.getInputStream(),
                getCharset(con.getContentType(), DEFAULT_ENCODING))) {
            while (true) {
                int ch = r.read();
                if (ch < 0) { break; }
                buf.append((char) ch);
            }
        }
        return buf.toString();
    }
}
