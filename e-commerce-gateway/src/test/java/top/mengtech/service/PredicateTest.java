package top.mengtech.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * java8 predicate 使用方法与思想
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class PredicateTest {
    public static List<String> MICRO_SERVICE = Arrays.asList("nacos","authority","gateway","feign","hystrix","e-commerce");


    @Test
    public void testPredicateTest(){
        Predicate<String> letterLengthLimit = s->s.length() > 5;

        MICRO_SERVICE.stream().filter(letterLengthLimit).forEach(System.out::println);
    }

    @Test
    public void testPredictAndTest(){
        Predicate<String> letterLengthLimit = s->s.length() > 5;

        Predicate<String> letterStartWith = s->s.startsWith("gate");

        MICRO_SERVICE.stream().filter(letterLengthLimit.and(letterStartWith)).forEach(System.out::println);
    }
}
