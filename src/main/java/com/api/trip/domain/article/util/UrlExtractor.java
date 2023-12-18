package com.api.trip.domain.article.util;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class UrlExtractor {

    private static final Pattern URL_PATTERN = Pattern.compile("https:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#()?&//=]*)");

    public static List<String> extractAll(String string) {
        Matcher matcher = URL_PATTERN.matcher(string);
        Stream<MatchResult> results = matcher.results();
        return results.map(MatchResult::group).toList();
    }
}
