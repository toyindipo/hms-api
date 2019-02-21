package com.oasis.hms.service;

import com.oasis.hms.dto.TagDto;

/**
 * Created by Toyin on 2/3/19.
 */
public interface TagService {
    TagDto getTagNumber();

    TagDto removeFromTagQueue(int count);

    int incTagNumber();
}
