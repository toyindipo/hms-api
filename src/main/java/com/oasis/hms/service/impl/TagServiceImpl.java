package com.oasis.hms.service.impl;

import com.oasis.hms.dto.TagDto;
import com.oasis.hms.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Toyin on 2/3/19.
 */
@Service
public class TagServiceImpl implements TagService {
    private AtomicInteger atomicInteger;
    private ConcurrentLinkedDeque<Integer> tagQueue;
    private TagDto tagDto;

    @Autowired
    public TagServiceImpl() {
        atomicInteger = new AtomicInteger(1);
        tagQueue = new ConcurrentLinkedDeque<>();
        tagDto = new TagDto();
    }

    @Override
    public TagDto getTagNumber() {
        return tagDto;
    }

    @Override
    public TagDto removeFromTagQueue(int count) {
        if (tagQueue.remove(count)) {
            tagDto.setLast(count);
        }
        if (tagQueue.isEmpty()) {
            tagDto.setNext(0);
        } else {
            tagDto.setNext(tagQueue.peek());
        }

        return tagDto;
    }

    @Override
    public int incTagNumber() {
        tagQueue.offer(atomicInteger.incrementAndGet());
        return atomicInteger.get();
    }
}
