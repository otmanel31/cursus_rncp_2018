<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>drones en folie!</title>
<s:url value="/resources/css/bonjour.css" var="coreStyle" />
<s:url value="/resources/css/bootstrap.css" var="bootstrapStyle" />
<link href="${bootstrapStyle}" rel="stylesheet" />
<link href="${coreStyle}" rel="stylesheet" />
</head>
<body>
<div class="container">
	<h2>espionne en t'amusant</h2>
	<div class="well">
		<form method="post" action="/funspy/saveArticle">
			<input type="hidden" name="id" id="id" value="${id}" />
			<div class="form-group">
				<label for="nom">nom article</label>
				<input type="text" class="form-control" id="nom" name="nom" value="${nom}"/>
			</div>
			<div class="form-group">
				<label for="description">description</label>
				<input type="text" class="form-control" id="description" name="description" value="${description}"/>
			</div>
			<div class="form-group">
				<label for="prix">prix</label>
				<input type="text" class="form-control" id="prix" name="prix" value="${prix}"/>
			</div>
			<div class="form-group">
				<label for="poids">poids</label>
				<input type="text" class="form-control" id="poids" name="poids"value="${poids}" />
			</div>
			<input type="submit" class="btn btn-primary" value="sauver" />
		</form>
	</div>
	<div class="well">
		<form method="get" action="/funspy/index">
			<div class="form-group">
				<label for="searchTerm">recherche</label>
				<input type="text" class="form-control" id="searchTerm" name="searchTerm"/>
			</div>
		</form>
	</div>
	<table border="1" class="table table-striped">
		<thead>
			<tr><th>nom</th><th>description</th><th>prix</th><th>poids</th><th>date sortie</th><th>actions</th></tr>
		</thead>
		<tbody>
			<c:forEach var="article" items="${articles}">
			<tr>
				<td>${article.nom}</td>
				<td>${article.description}</td>
				<td>${article.prix}</td>
				<td>${article.poids}</td>
				<td>${article.dateSortie}</td>
				<td>
					<a href="editArticle/${article.id}" class="btn btn-success">editer</a>
					<form method="post" action="deleteArticle/${article.id}">
						<input type="submit" class="btn btn-danger" value="supprimer" />
					</form>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
</body>
</html>