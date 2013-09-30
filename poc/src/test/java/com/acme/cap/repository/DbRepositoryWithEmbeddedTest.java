package com.acme.cap.repository;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:META-INF/spring/embedded.xml")
public class DbRepositoryWithEmbeddedTest extends DbRepositoryServerTest {

}
