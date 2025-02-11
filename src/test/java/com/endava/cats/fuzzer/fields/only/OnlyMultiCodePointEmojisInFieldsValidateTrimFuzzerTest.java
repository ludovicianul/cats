package com.endava.cats.fuzzer.fields.only;

import com.endava.cats.args.FilesArguments;
import com.endava.cats.args.FilterArguments;
import com.endava.cats.fuzzer.http.ResponseCodeFamily;
import com.endava.cats.http.HttpMethod;
import com.endava.cats.io.ServiceCaller;
import com.endava.cats.model.FuzzingData;
import com.endava.cats.model.FuzzingStrategy;
import com.endava.cats.report.TestCaseListener;
import com.endava.cats.util.CatsUtil;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

class OnlyMultiCodePointEmojisInFieldsValidateTrimFuzzerTest {
    private final CatsUtil catsUtil = new CatsUtil(null);
    @Mock
    private ServiceCaller serviceCaller;
    @Mock
    private TestCaseListener testCaseListener;
    @Mock
    private FilesArguments filesArguments;
    @Mock
    private FilterArguments filterArguments;

    private OnlyMultiCodePointEmojisInFieldsValidateTrimFuzzer onlyMultiCodePointEmojisInFieldsValidateTrimFuzzer;

    @BeforeEach
    void setup() {
        onlyMultiCodePointEmojisInFieldsValidateTrimFuzzer = new OnlyMultiCodePointEmojisInFieldsValidateTrimFuzzer(serviceCaller, testCaseListener, catsUtil, filesArguments, filterArguments);
    }

    @Test
    void shouldOverrideDefaultMethods() {
        Assertions.assertThat(onlyMultiCodePointEmojisInFieldsValidateTrimFuzzer.getExpectedHttpCodeWhenFuzzedValueNotMatchesPattern()).isEqualTo(ResponseCodeFamily.FOURXX);
        Assertions.assertThat(onlyMultiCodePointEmojisInFieldsValidateTrimFuzzer.getExpectedHttpCodeWhenOptionalFieldsAreFuzzed()).isEqualTo(ResponseCodeFamily.FOURXX);
        Assertions.assertThat(onlyMultiCodePointEmojisInFieldsValidateTrimFuzzer.skipForHttpMethods()).containsExactly(HttpMethod.GET, HttpMethod.DELETE);
        Assertions.assertThat(onlyMultiCodePointEmojisInFieldsValidateTrimFuzzer.description()).isNotNull();
        Assertions.assertThat(onlyMultiCodePointEmojisInFieldsValidateTrimFuzzer.typeOfDataSentToTheService()).isNotNull();
        Assertions.assertThat(onlyMultiCodePointEmojisInFieldsValidateTrimFuzzer.getInvisibleChars()).contains("\uD83D\uDC69\uD83C\uDFFE");
    }

    @Test
    void shouldReturnProperLengthWhenNoMinLLength() {
        FuzzingData data = Mockito.mock(FuzzingData.class);
        Map<String, Schema> schemaMap = new HashMap<>();
        StringSchema stringSchema = new StringSchema();
        schemaMap.put("schema", stringSchema);
        Mockito.when(data.getRequestPropertyTypes()).thenReturn(schemaMap);

        FuzzingStrategy fuzzingStrategy = onlyMultiCodePointEmojisInFieldsValidateTrimFuzzer.getFieldFuzzingStrategy(data, "schema").get(1);
        Assertions.assertThat(fuzzingStrategy.name()).isEqualTo(FuzzingStrategy.replace().name());
        Assertions.assertThat(fuzzingStrategy.getData()).isEqualTo("\uD83D\uDC68\u200D\uD83C\uDFED️");
    }

    @Test
    void shouldReturnProperLengthWhenMinValue() {
        FuzzingData data = Mockito.mock(FuzzingData.class);
        Map<String, Schema> schemaMap = new HashMap<>();
        StringSchema stringSchema = new StringSchema();
        stringSchema.setMinLength(5);

        schemaMap.put("schema", stringSchema);
        Mockito.when(data.getRequestPropertyTypes()).thenReturn(schemaMap);
        String theEmoji = "\uD83D\uDC68\u200D\uD83C\uDFED️";
        FuzzingStrategy fuzzingStrategy = onlyMultiCodePointEmojisInFieldsValidateTrimFuzzer.getFieldFuzzingStrategy(data, "schema").get(1);
        Assertions.assertThat(fuzzingStrategy.name()).isEqualTo(FuzzingStrategy.replace().name());
        Assertions.assertThat(fuzzingStrategy.getData()).isEqualTo(StringUtils.repeat(theEmoji, (stringSchema.getMinLength() / theEmoji.length()) + 1));
    }
}
