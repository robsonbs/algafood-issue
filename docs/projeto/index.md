# Sobre o contexto

Uma livraria precisa de meios para manter seu acervo organizado e suas vendas registradas.

## Controles

- `POST /livros`: A rota deve receber `titulo`, `autor`, `descrição`, `isbn`, `valor`, `categoria`(s) no corpo da
  requisição. Ao cadastrar deve ser criado o registro no banco de dados contendo os seguintes campos: `id`, `titulo`
  ,`autor`, `descrição`, `isbn`, `valor`, `categoria_id`.

  **Dica**: Para a categoria, você deve criar uma nova tabela, que terá os campos `id`, `title`.

- `GET /livros` lista os livros cadastrados até agora.
- `GET /livros/{id}`: mostra os dados de um book de `id`.
- `DELETE /livros/{id}`: exclui o book com base no id passado.
- `PUT /livros/{id}`: atualiza um book de `id` recebendo na requisição: `titulo`, `autor`, `descrição`, `isbn`, `valor`, `categoria`.



