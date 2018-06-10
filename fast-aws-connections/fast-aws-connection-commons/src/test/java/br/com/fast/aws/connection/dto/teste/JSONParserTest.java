package br.com.fast.aws.connection.dto.teste;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.com.fast.aws.connection.commons.testes.dto.PassagemDTO;
import br.com.fast.aws.connection.commons.utils.JSONParser;

public class JSONParserTest {

    private List<String> jsonList;

    @Test
    public void deveConverterListJsonParaDTO() {
        List<PassagemDTO> dtoList = new ArrayList<>();
        jsonList = new ArrayList<>();

        PassagemDTO dto1 = criaDTO(1010L, 2001L);

        PassagemDTO dto2 = criaDTO(1011L, 2001L);

        jsonList.add(dto1.toJSON());
        jsonList.add(dto2.toJSON());

        dtoList = JSONParser.createFromJsonList(jsonList, PassagemDTO.class);

        assertEquals(dto1.getPassagemId(), dtoList.get(0).getPassagemId());
        assertEquals(dto2.getPassagemId(), dtoList.get(1).getPassagemId());

    }

    @Test
    public void deveConverterDTOparaJSON() {
        PassagemDTO dto = criaDTO(1010L, 2001L);

        String dtoJson = JSONParser.toJSON(dto);

        assertEquals(dto.toJSON(), dtoJson);

    }

    @Test
    public void deveConverterDTOparaYAMLdeObjeto() {
        PassagemDTO dto = criaDTO(1010L, 2001L);

        String yaml = JSONParser.toYAML(dto);
        System.out.println(yaml);

    }

    @Test
    public void deveConverterDTOparaYAMLdeJSON() {
        PassagemDTO dto = criaDTO(1010L, 2001L);
        String json = dto.toJSON();

        String yaml = JSONParser.toYAML(json);
        System.out.println(yaml);

    }

    private PassagemDTO criaDTO(Long passagemId, Long tagId) {
        PassagemDTO dto = new PassagemDTO();
        dto.setPassagemId(passagemId);
        dto.setTagId(tagId);
        dto.setDatahora(new Date().getTime());
        dto.setPassAutomatica(true);
        return dto;
    }

}
