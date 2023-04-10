package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.util.List;
import dao.FilmeDAO;
import model.Filme;
import spark.Request;
import spark.Response;


public class FilmeService {

	private FilmeDAO filmeDAO = new FilmeDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_NOME = 2;
	private final int FORM_ORDERBY_GENERO = 3;
	
	
	
	public FilmeService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Filme(), FORM_ORDERBY_NOME);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Filme(), orderBy);
	}

	
	public void makeForm(int tipo, Filme filme, int orderBy) {
		String nomeArquivo = "src/main/resources/index.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umFilme = "";
				
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/filme/";
			if (tipo == FORM_INSERT){
				action += "insert";
			} else {
				action += "update/" + filme.getId();
			}
			umFilme += "\t<form class=\"form-cadastro\" action=\"" + action + "\" method=\"post\" id=\"form-descricao\">";
			umFilme += "\t<div class=\"form-group row\">";
			umFilme += "\t\t<label for=\"inputId\">Id</label>";
			umFilme += "\t\t\t<input class=\"form-control\" type=\"text\" id=\"inputId\" placeholder=\"Id\" readonly>";
			umFilme += "\t\t</div>";
			umFilme += "\t\t<div class=\"col-sm-8\">";
			umFilme += "\t\t\t<label for=\"inputNome\">Nome</label>";
			umFilme += "\t\t<input type=\"text\" class=\"form-control\" id=\"inputNome\" required\r\n"
					+ "                        placeholder=\"Informe o nome do filme\" value=\"" + filme.getNome() + "\">";
			umFilme += "\t\t</div>";
			umFilme += "\t\t\t</div>";
			umFilme += "\t\t\t<div class=\"form-goup row\">";
			umFilme += "\t\t\t<div class=\"col-sm-3\">";
			umFilme += "\t\t<label for=\"inputGenero\">Gênero</label>";
			umFilme += "\t\t<input type=\"text\" class=\"form-control\" id=\"inputGenero\" required placeholder=\"Gênero\" value=\"" + filme.getGenero() + "\">";
			umFilme += "\t\t\t</div>";
			umFilme += "\t\t\t<div class=\"col-sm-7\">";
			umFilme += "\t\t\t<label for=\"inputDiretor\">Diretor</label>";
			umFilme += "\t\t<input type=\"text\" class=\"form-control\" id=\"inputDiretor\" required placeholder=\"Diretor\" value=\"" + filme.getDiretor() + "\">";
			umFilme += "\t</div>";
			umFilme += "\t<div class=\"col-sm-2\">";
			umFilme += "<label for=\"inputLancamento\">Data de lançamento</label>";
			umFilme += "<input type=\"date\" class=\"form-control\" id=\"inputLancamento\" value=\"" + filme.getDataLancamento() + "\">";
			umFilme += "</div>";
			umFilme += "</div>";
			umFilme += "</form>";
			
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-FILME>", umFilme);
		
		String list = new String("<thead>");
		list += "\n<tr>\n" +
				"\n<th scope=\"col\" class=\"/filme/list/1\">Id</th>\n" +
    			"\n<th scope=\"col\" class=\"/filme/list/2\">Nome</th>\n" + 
        		"\t<th scope=\"col\" class=\"/filme/list/3\">Genêro</th>\n" +
        		"\t<th scope=\"col\">Diretor</th>\n" +
        		"\t<th scope=\"col\">Data de Lançamento</th>\n" +
        		"</tr>\n";
		
		List<Filme> filmes;
		if (orderBy == FORM_ORDERBY_ID) {                 	filmes = filmeDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_NOME) {			filmes = filmeDAO.getOrderByNome();
		} else if (orderBy == FORM_ORDERBY_GENERO) {		filmes = filmeDAO.getOrderByGenero();
		} else {											filmes = filmeDAO.get();
		}

		//int i = 0;
		//String bgcolor = "";
		for (Filme p : filmes) {
			//bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr>\n" + 
            		  "\t<td scope=\"row\">" + p.getId() + "</td>\n" +
            		  "\t<td>" + p.getNome() + "</td>\n" +
            		  "\t<td>" + p.getGenero() + "</td>\n" +
            		  "\t<td>" + p.getDiretor() + "</td>n" +
            		  "\t<td>" + p.getDataLancamento() + "</td>\n";
		}
		list += "</tr>";		
		form = form.replaceFirst("<LISTAR-PRODUTO>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String nome = request.queryParams("nome");
		String genero = request.queryParams("genero");
		String diretor = request.queryParams("diretor");
		LocalDate lancamento = LocalDate.parse(request.queryParams("lancamento"));
		
		String resp = "";
		
		Filme filme = new Filme(-1, nome, genero, diretor, lancamento);
		
		if(filmeDAO.insert(filme) == true) {
            resp = "Filme (" + nome + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Filme (" + nome + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Filme produto = (Filme) filmeDAO.get(id);
		
		if (produto != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, produto, FORM_ORDERBY_NOME);
        } else {
            response.status(404); // 404 Not found
            String resp = "Filme " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Filme produto = (Filme) filmeDAO.get(id);
		
		if (produto != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, produto, FORM_ORDERBY_NOME);
        } else {
            response.status(404); // 404 Not found
            String resp = "Filme " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Filme filme = filmeDAO.get(id);
        String resp = "";       

        if (filme != null) {
        	filme.setNome(request.queryParams("descricao"));
        	filme.setGenero(request.queryParams("preco"));
        	filme.setDiretor(request.queryParams("quantidade"));
        	filme.setDataLancamento(LocalDate.parse(request.queryParams("lancamento")));
        	filmeDAO.update(filme);
        	response.status(200); // success
            resp = "Filme (ID " + filme.getId() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Filme (ID \" + filme.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Filme filme = filmeDAO.get(id);
        String resp = "";       

        if (filme != null) {
            filmeDAO.delete(id);
            response.status(200); // success
            resp = "Filme (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Filme (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}