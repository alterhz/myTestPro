package com.magazine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ObjectMapperTest {
    @Test
    void testJson() throws JsonProcessingException {
        List<Integer> list = List.of(1, 3, 5, 9);
        List<ImmutablePair<Integer, String>> l = new ArrayList<>();
        l.add(ImmutablePair.of(1, "akd"));
        l.add(ImmutablePair.of(2, "ak22d"));
        l.add(ImmutablePair.of(3, "ak3d"));
        l.add(ImmutablePair.of(4, "a1kd"));
        final ObjectMapper objectMapper = new ObjectMapper();
        final String s = objectMapper.writeValueAsString(l);
        System.out.println("s = " + s);

        final List<ImmutablePair<Integer, String>> list1 = objectMapper.readValue(s, List.class);
        System.out.println("list1 = " + list1);
        System.out.println("!!! >> here the objects inside list are linkedHashmap");
    }
}
