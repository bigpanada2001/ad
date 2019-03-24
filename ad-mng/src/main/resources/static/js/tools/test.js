/**
 * 简单单元测试
 */
var UnitTest = {
    assertTrue: function(result) {
        result = !! result;
        if (result == true) return;
        throw "error";
    },
    assertFalse: function(result) {
        result = !! result;
        if (result == false) return;
        throw "error";
    },
    assertEqual: function(v1, v2) {
        if (v1 == v2) return;
        throw "error";
    }
};

console.log("[Unit Test Begin]");

UnitTest.assertTrue(StringUtil.isNumeric("100"));
UnitTest.assertTrue(StringUtil.isNumeric("100.321"));

UnitTest.assertTrue(StringUtil.isWrapped("''"));
UnitTest.assertTrue(StringUtil.isWrapped("'xxx'"));
UnitTest.assertFalse(StringUtil.isWrapped("'''"));

UnitTest.assertTrue(StringUtil.isWrapped('""'));
UnitTest.assertTrue(StringUtil.isWrapped('"xxx"'));
UnitTest.assertFalse(StringUtil.isWrapped('"""'));

UnitTest.assertTrue(StringUtil.isArray("[]"));
UnitTest.assertTrue(StringUtil.isArray("[xxx]"));
UnitTest.assertFalse(StringUtil.isArray("[xxx"));
UnitTest.assertFalse(StringUtil.isArray("xxx]"));

UnitTest.assertEqual(StringUtil.removeHeadTail("[xxx]"), "xxx");

console.log("[Unit Test Complete]");