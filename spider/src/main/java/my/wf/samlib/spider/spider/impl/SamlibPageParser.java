package my.wf.samlib.spider.spider.impl;

import my.wf.samlib.spider.engine.IpCheckState;
import my.wf.samlib.spider.model.WritingData;
import my.wf.samlib.spider.spider.PageParser;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SamlibPageParser implements PageParser {


    private static final Pattern namePattern = Pattern.compile("<center>\\s*<h3>(?<authorName>.*?):<br>");
    private static final Pattern writingPattern = Pattern.compile(
            "<DL><DT><li>(?:<font.*?>.*?</font>)?\\s*(<b>(?<Authors>.*?)\\s*</b>\\s*)?<A HREF=(?<LinkToText>.*?)" +
                    "><b>\\s*(?<NameOfText>.*?)\\s*</b></A>.*?<b>(?<SizeOfText>\\d+)k</b>.*?<small>(?:Оценка:<b>" +
                    "(?<DescriptionOfRating>(?<rating>\\d+(?:\\.\\d+)?).*?)</b>.*?)?\\s*\"@*(?<Section>.*?)\"\\s*" +
                    "(?<Genres>.*?)?\\s*(?:<A HREF=\"(?<LinkToComments>.*?)\">Комментарии:\\s*(?<CommentsDescription>" +
                    "(?<CommentCount>\\d+).*?)</A>\\s*)?</small>.*?(?:<br><DD>(<font(.*?)?>)?(?<Description>.*?))?" +
                    "(</font><DD>.*?)?</DL>");
    private static final Pattern ipPattern = Pattern.compile("(?<ip>\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
    //private static final Pattern checkStatePattern = Pattern.compile("<pre>\\s*(?<info>.*?IP:\\s(?<ip>\\d{1,3}\\
    // .\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*?1\\.\\s*(?<inSpam>.*?)2\\.(?<Blocked>.*?))</pre>");
    private static final Pattern infoPattern =
            Pattern.compile("(<pre>)?.*?(?<info>IP:\\s(?<ip>\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*?)(</pre>|$)");
    private static final String NOT_IN_SPAM = "1. не занесен в спам-лист (not in spam-list)";
    private static final String NOT_IS_BLOCKED = "не блокирован";

    @Override
    public Set<WritingData> parseAuthorPage(String pageString) {
        Matcher matcher = writingPattern.matcher(pageString);
        Set<WritingData> writings = new HashSet<>();
        while (matcher.find()) {
            WritingData writing = fromMatcher(matcher);
            writings.add(writing);
        }
        return writings;
    }

    @Override
    public String parseAuthorName(String pageString) {
        Matcher matcher = namePattern.matcher(pageString);
        return matcher.find() ? matcher.group("authorName") : null;
    }


    @Override
    public IpCheckState parseIpCheckState(String checkPageString) {
        IpCheckState ipCheckState = new IpCheckState();
        String preparedString = removeSpaces(checkPageString);

        if (null == checkPageString) {
            ipCheckState.setOtherError(true);
            ipCheckState.setInfo("EMPTY check By IP");
        } else {
            Matcher infoMatcher = infoPattern.matcher(preparedString);
            if (infoMatcher.find()) {
                ipCheckState.setInfo(infoMatcher.group("info"));
                ipCheckState.setIp(infoMatcher.group("ip"));
            }
            ipCheckState.setInSpamList(!preparedString.contains(NOT_IN_SPAM));
            ipCheckState.setBlocked(!preparedString.contains(NOT_IS_BLOCKED));
        }

        return ipCheckState;
    }

    private String removeSpaces(String page) {
        String s = Pattern.compile("\\s+")
                          .matcher(page)
                          .replaceAll(" ");
        return Pattern.compile(">\\s+<")
                      .matcher(s)
                      .replaceAll("><");
    }

    private WritingData fromMatcher(Matcher matcher) {
        WritingData writingData = new WritingData();
        writingData.setName(matcher.group("NameOfText"));
        writingData.setUrl(matcher.group("LinkToText"));
        String description = matcher.group("Description");
        if (null != description) {
            writingData.setDescription(description.replaceAll("\\<[^>]*>", ""));
        }
        writingData.setSize(matcher.group("SizeOfText"));
        writingData.setGroupName(matcher.group("Section"));
        return writingData;
    }
}
