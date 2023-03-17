package app;

import static spark.Spark.*;

public class Aplicacao {
	
	private static FilmeService produtoService = new FilmeService();
	
    public static void main(String[] args) {
        port(6789);

        post("/produto", (request, response) -> filmeService.add(request, response));

        get("/produto/:id", (request, response) -> filmeService.get(request, response));

        get("/produto/update/:id", (request, response) -> filmeService.update(request, response));

        get("/produto/delete/:id", (request, response) -> filmeService.remove(request, response));

        get("/produto", (request, response) -> filmeService.getAll(request, response));
               
    }
}
