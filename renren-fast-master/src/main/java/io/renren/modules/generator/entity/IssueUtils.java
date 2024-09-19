package io.renren.modules.generator.entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IssueUtils {

    // 方法：将以逗号分隔的字符串转换为List<String>
    public static List<String> parseRelatedIssueIds(String relatedIssueIds) {
        if (relatedIssueIds != null && !relatedIssueIds.trim().isEmpty()) {
            return Arrays.stream(relatedIssueIds.split(","))
                    .map(String::trim) // 去除每个编号的空格
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(); // 如果没有记录，则返回空列表
    }
}

