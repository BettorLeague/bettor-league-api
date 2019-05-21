package com.bettorleague.server.rest;

import com.bettorleague.server.dto.news.NewsResponse;
import com.bettorleague.server.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@Validated
public class NewsController {

    @Autowired
    RestTemplate httpClient;

    @Autowired
    ModelMapper mapper;


    @RequestMapping(path = "/news", method = RequestMethod.GET)
    public ResponseEntity<List<NewsResponse>> getNews(@RequestParam(value = "searchQuery", required = true) String searchQuery,
                                                      @NotNull @Valid @Max(5000) @Min(500) @RequestParam(value = "imageSize", defaultValue = "1280") Integer imageSize) {
        String response = httpClient.getForObject("https://news.google.com/rss/search?q=" + searchQuery + "&hl=fr&gl=FR&ceid=FR:fr", String.class);

        if (response == null) {
            throw new ResourceNotFoundException("Search", "query", searchQuery);
        }

        JSONObject json = XML.toJSONObject(response);
        JSONArray items = json.optJSONObject("rss").optJSONObject("channel").optJSONArray("item");

        if (items == null) {
            throw new ResourceNotFoundException("Items", "query", searchQuery);
        }

        List<NewsResponse> filtered = arrayToStream(items)
                .map(JSONObject.class::cast)
                .filter(item -> item.has("media:content"))
                .filter(item -> !item.get("description").toString().contains("Google"))
                .map(item -> {
                    JSONObject media = item.getJSONObject("media:content");
                    media.put("width", imageSize);
                    media.put("height", imageSize);
                    media.put("url", media.get("url").toString().replace("150", String.valueOf(imageSize)));
                    item.put("media", media);
                    item.remove("media:content");
                    item.remove("guid");
                    return item.toMap();
                })
                .map(item -> mapper.map(item, NewsResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }

    @NotNull
    private static Stream<Object> arrayToStream(JSONArray array) {
        return StreamSupport.stream(array.spliterator(), false);
    }

}
