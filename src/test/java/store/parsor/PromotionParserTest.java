package store.parsor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import store.utils.parsor.CsvReader;
import store.utils.parsor.PromotionFieldsDto;
import store.utils.parsor.PromotionParser;

class PromotionParserTest {

    @Test
    void 프로모션_객체_생성_테스트() throws IOException {
        // given
        String data = "name,buy,get,start_date,end_date\n탄산2+1,2,1,2024-01-01,2024-12-31";
        PromotionParser promotionParser = new PromotionParser(
                new CsvReader(new BufferedReader(new StringReader(data)), true));

        // when
        PromotionFieldsDto promotionFieldsDto = promotionParser.nextPromotion();

        // then
        Assertions.assertEquals("탄산2+1", promotionFieldsDto.name());
        Assertions.assertEquals(2, promotionFieldsDto.buy());
        Assertions.assertEquals(1, promotionFieldsDto.get());

        Assertions.assertEquals(2024, promotionFieldsDto.startDate().getYear());
        Assertions.assertEquals(1, promotionFieldsDto.startDate().getMonthValue());
        Assertions.assertEquals(1, promotionFieldsDto.startDate().getDayOfMonth());

        Assertions.assertEquals(2024, promotionFieldsDto.endDate().getYear());
        Assertions.assertEquals(12, promotionFieldsDto.endDate().getMonthValue());
        Assertions.assertEquals(31, promotionFieldsDto.endDate().getDayOfMonth());
    }

}