package modulos.produto;

//Importações estáticas recomendas pelo RestAssured
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

//Demais importações
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Testes de API Rest do modulo de Produto")
public class ProdutoTest {
    @Test
    @DisplayName("Validar os limites proibidos do valor do Produto")
    public void testValidarLimitesProibidosValorProduto(){
        // Configurando os dados da API Rest da Lojinha
        baseURI = "http://165.227.93.41";
        //port = 8080;
        basePath = "/lojinha";

        // Obter o token do usuario admin
        String token =
            given()
                    .contentType(ContentType.JSON) //ContentType.JSON é equivalente ao application/json no Postman
                    .body("{\n" +
                        "  \"usuarioLogin\": \"admin\",\n" +
                        "  \"usuarioSenha\": \"admin\"\n" +
                        "}")
            .when()
                    .post("/v2/login")
            .then()
                    .extract()
                        .path("data.token");

        /* Tentar inseir um produto com valor 0.00 e validar que a mensagem de erro foi apresentada e o
            status code retornado foi 422 */
            given()
                    .contentType(ContentType.JSON)
                    .header("token", token)
                    .body("{\n" +
                            "  \"produtoNome\": \"SmartTV\",\n" +
                            "  \"produtoValor\": 0.00,\n" +
                            "  \"produtoCores\": [\n" +
                            "    \"prata\"\n" +
                            "  ],\n" +
                            "  \"produtoUrlMock\": \"\",\n" +
                            "  \"componentes\": [\n" +
                            "        {\n" +
                            "      \"componenteNome\": \"controle\",\n" +
                            "      \"componenteQuantidade\": 1\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}")
            .when()
                    .post("/v2/produtos")
            .then()
                    .assertThat() //Para validar a mensagem de erro
                        .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                        .statusCode(422);

    }
}
