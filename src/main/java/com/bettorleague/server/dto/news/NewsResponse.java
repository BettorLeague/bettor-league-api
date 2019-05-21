package com.bettorleague.server.dto.news;
import lombok.Data;

import java.util.Date;

@Data
class NewsMedia{
    private Integer width;
    private Integer height;
    private String url;
}

@Data
class NewsSource {
    private String url;
    private String content;
}

@Data
public class NewsResponse{
    private String title;
    private String pubDate;
    private NewsSource source;
    private String link;
    private String description;
    private NewsMedia media;


}


