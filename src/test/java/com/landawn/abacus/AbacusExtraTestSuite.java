package com.landawn.abacus;

import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/** Optional IDE entry point; Maven discovers the unit test classes directly. */
@Suite
@SelectPackages("com.landawn.abacus.util")
@IncludeClassNamePatterns(".*Test")
@IncludeEngines("junit-jupiter")
@ExcludeTags("slow-test")
public class AbacusExtraTestSuite {
}
