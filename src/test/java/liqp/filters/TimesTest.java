package liqp.filters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import liqp.Template;
import liqp.TemplateParser;

public class TimesTest {

    @Test
    public void applyTest() throws RecognitionException {

        String[][] tests = {
                {"{{ 8 | times: 2 }}", "16"},
                {"{{ 8 | times: 3 }}", "24"},
                {"{{ 8 | times: 3. }}", "24.0"},
                {"{{ 8 | times: '3.0' }}", "24.0"},
                {"{{ 8 | times: 2.0 }}", "16.0"},
                {"{{ foo | times: 4 }}", "0"},
                {"{{ '0.1' | times: 3 }}", "0.3"},
        };

        for (String[] test : tests) {

            Template template = TemplateParser.DEFAULT.parse(test[0]);
            String rendered = String.valueOf(template.render());

            assertThat(rendered, is(test[1]));
        }
    }

    @Test(expected=RuntimeException.class)
    public void applyTestInvalid1() {
        Filters.COMMON_FILTERS.get("times").apply(1);
    }

    @Test(expected=RuntimeException.class)
    public void applyTestInvalid2() {
        Filters.COMMON_FILTERS.get("times").apply(1, 2, 3);
    }

    /*
     * def test_times
     *   assert_template_result "12", "{{ 3 | times:4 }}"
     *   assert_template_result "0", "{{ 'foo' | times:4 }}"
     *   assert_template_result "6", "{{ '2.1' | times:3 | replace: '.','-' | plus:0}}"
     *   assert_template_result "7.25", "{{ 0.0725 | times:100 }}"
     *   assert_template_result "-7.25", '{{ "-0.0725" | times:100 }}'
     *   assert_template_result "7.25", '{{ "-0.0725" | times: -100 }}'
     * ???
     *   assert_template_result "4", "{{ price | times:2 }}", 'price' => NumberLikeThing.new(2)
     * end
     */
    @Test
    public void applyOriginalTest() {

        Filter filter = Filters.COMMON_FILTERS.get("times");

        assertThat(filter.apply(3L, 4L), is((Object)12L));
        // assert_template_result "0", "{{ 'foo' | times:4 }}" // see: applyTest()
        assertTrue(String.valueOf(filter.apply(2.1, 3L)).matches("6[.,]3"));
        assertEquals("7.25", filter.apply(0.0725, 100));
        assertEquals("-7.25", filter.apply(-0.0725, 100));
        assertEquals("7.25", filter.apply(-0.0725, -100));
    }
}
