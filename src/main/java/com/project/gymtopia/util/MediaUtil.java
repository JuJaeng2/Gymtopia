package com.project.gymtopia.util;

import com.project.gymtopia.member.data.entity.Media;
import com.project.gymtopia.member.data.model.MediaResponse;
import java.util.ArrayList;
import java.util.List;

public class MediaUtil {

  public static MediaResponse classifyMedia(List<Media> mediaList) {

    List<String> imageUrlList = new ArrayList<>();
    String videoUrl = null;

    for (Media media : mediaList) {
      String url = media.getUrl();
      int idx = url.lastIndexOf(".");
      String extension = url.substring(idx + 1);
      if (extension.equals("mp4")) {
        videoUrl = url;
        continue;
      }
      imageUrlList.add(url);
    }

    return MediaResponse.builder()
        .imageUrlList(imageUrlList)
        .videoUrl(videoUrl)
        .build();
  }

}
