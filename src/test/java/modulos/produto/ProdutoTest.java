package modulos.produto;

//Importações estáticas recomendas pelo RestAssured
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

//Demais importações
import dataFactory.ProdutoDataFactory;
import dataFactory.UsuarioDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Testes de API Rest do modulo de Produto")
public class ProdutoTest {
    private String token;

    @BeforeEach
    public void beforeEach(){
        // Configurando os dados da API Rest da Lojinha
        baseURI = "http://165.227.93.41";
        //port = 8080;
        basePath = "/lojinha";

        // Obter o token do usuario admin
           this.token =
                given()
                        .contentType(ContentType.JSON) //ContentType.JSON é equivalente ao application/json no Postman
                        .body(UsuarioDataFactory.criarUsuarioAdministrador())
                .when()
                        .post("/v2/login")
                .then()
                        .extract()
                            .path("data.token");

    }

    @Test
    @DisplayName("Validar que o valor do produto igual a 0.00 não é permitido")
    public void testValidarLimitesZeradoProibidoValorProduto(){

        /* Tentar inseir um produto com valor 0.00 e validar que a mensagem de erro foi apresentada e o
            status code retornado foi 422 */

            given()
                    .contentType(ContentType.JSON)
                    .header("token", this.token)
                    .body(ProdutoDataFactory.criarProdutoComumComOValorIgualA(0.00))
            .when()
                    .post("/v2/produtos")
            .then()
                    .assertThat() //Para validar a mensagem de erro
                        .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                        .statusCode(422);

    }


    @Test
    @DisplayName("Validar que o valor do produto igual a 7000.01 não é permitido")
    public void testValidarLimitesMaiorSeteMilProibidoValorProduto(){
        /* Tentar inseir um produto com valor 7000.01 e validar que a mensagem de erro foi apresentada e o
            status code retornado foi 422 */
        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(ProdutoDataFactory.criarProdutoComumComOValorIgualA(7000.01))
        .when()
                .post("/v2/produtos")
        .then()
                .assertThat() //Para validar a mensagem de erro
                    .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                    .statusCode(422);

    }

}
