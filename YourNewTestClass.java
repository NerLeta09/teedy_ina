package com.sismics.docs;

import com.sismics.docs.core.service.UserServiceTest;
import com.sismics.docs.web.controller.UserControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    UserServiceTest.class,
    UserControllerTest.class
})
public class YourNewTestClass {
    // This class remains empty, it is used only as a holder for the above annotations
}