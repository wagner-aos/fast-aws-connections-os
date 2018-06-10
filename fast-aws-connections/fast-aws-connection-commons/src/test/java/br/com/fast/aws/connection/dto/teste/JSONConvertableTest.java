package br.com.fast.aws.connection.dto.teste;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import br.com.fast.aws.connection.commons.interfaces.JSONConvertable;
import br.com.fast.aws.connection.commons.testes.dto.PassagemDTO;

public class JSONConvertableTest {

    PassagemDTO dto;
    String json;
    String jsonWithNullAttributes;

    PassagemDTO dto8601;
    String json8601;
    String jsonWithNullAttributes8601;

    @Before
    public void setUp() {

        dto = new PassagemDTO();
        dto.setPassagemId(1010L);
        dto.setTagId(2000L);
        dto.setDatahora(new Date().getTime());
        dto.setPassAutomatica(true);

        json = dto.toJSON();
        jsonWithNullAttributes = dto.toJSONWithNullAttributes();
        /*
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS_8601.getFormato());
        String data8601 = sdf.format(new Date().getTime());
        
        System.out.println("TESTE 8601: " + data8601);*/

        dto8601 = new PassagemDTO();
        dto8601.setPassagemId(1010L);
        dto8601.setTagId(2000L);
        dto8601.setData(new Date());
        dto8601.setPassAutomatica(true);

        json8601 = dto8601.toJSONWith8601DateFormat();
        jsonWithNullAttributes8601 = dto8601.toJSONWithNullAttributesAnd8601DateFormat();

    }

    @Test
    public void deveConverterDTOparaJson() {
        String json = dto.toJSON();

        System.out.println("JSON: " + json);

        assertEquals(this.json, json);
    }

    @Test
    public void deveConverterDTOparaJsonComAtributosNulos() {
        String jsonWithNullAttributes = dto.toJSONWithNullAttributes();

        System.out.println("JSON null: " + jsonWithNullAttributes);

        assertEquals(this.jsonWithNullAttributes, jsonWithNullAttributes);
    }

    @Test
    public void deveConverterJsonParaDTO() {
        PassagemDTO dto = new PassagemDTO().createFromJSON(json, PassagemDTO.class);

        assertEquals(this.dto.getPassagemId(), dto.getPassagemId());
        assertEquals(this.dto.getTagId(), dto.getTagId());
        assertEquals(this.dto.isPassAutomatica(), dto.isPassAutomatica());
        assertEquals(this.dto.getCatCobrada(), dto.getCatCobrada());

    }

    @Test
    public void deveConverterJsonParaDTOStatic() {

        PassagemDTO dto = JSONConvertable.createFromJsonStatic(json, PassagemDTO.class);

        assertEquals(this.dto.getPassagemId(), dto.getPassagemId());
        assertEquals(this.dto.getTagId(), dto.getTagId());
        assertEquals(this.dto.isPassAutomatica(), dto.isPassAutomatica());
        assertEquals(this.dto.getCatCobrada(), dto.getCatCobrada());

    }

    // Testes com ISO 8601 Date Format

    @Test
    public void deveConverterDTOparaJsonWith8601DateFormat() {
        String json = dto8601.toJSONWith8601DateFormat();
        assertEquals(this.json8601, json);

        System.out.println("JSON 8601: " + json);
    }

    @Test
    public void deveConverterDTOparaJsonComAtributosNulosWith8601DateFormat() {
        String jsonWithNullAttributes = dto8601.toJSONWithNullAttributesAnd8601DateFormat();
        assertEquals(this.jsonWithNullAttributes8601, jsonWithNullAttributes);

        System.out.println("JSON 8601 withNull: " + jsonWithNullAttributes);
    }

    @Test
    public void deveConverterJsonParaDTOWith8601DateFormat() {
        PassagemDTO dto = new PassagemDTO().createFromJSONwith8601DateFormat(json8601, PassagemDTO.class);

        assertEquals(this.dto8601.getPassagemId(), dto.getPassagemId());
        assertEquals(this.dto8601.getTagId(), dto.getTagId());
        assertEquals(this.dto8601.isPassAutomatica(), dto.isPassAutomatica());
        assertEquals(this.dto8601.getCatCobrada(), dto.getCatCobrada());

        System.out.println("DTO 8601: " + dto.getData());

    }

    @Test
    public void deveConverterJsonParaDTOStaticWith8601DateFormat() {

        PassagemDTO dto = JSONConvertable.createFromJsonStaticWith8601DateFormat(json8601, PassagemDTO.class);

        assertEquals(this.dto8601.getPassagemId(), dto.getPassagemId());
        assertEquals(this.dto8601.getTagId(), dto.getTagId());
        assertEquals(this.dto8601.isPassAutomatica(), dto.isPassAutomatica());
        assertEquals(this.dto8601.getCatCobrada(), dto.getCatCobrada());

        System.out.println(">>> DTO 8601 : " + dto.getData());

        System.out.println(">>> JSON 8601 : " + dto.toJSONWith8601DateFormat());

    }

}
