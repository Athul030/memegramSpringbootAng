package com.athul.memegramspring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class VideoCallDTO {

    private String type;
    private Object content;
}
