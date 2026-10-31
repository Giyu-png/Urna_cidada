# Fotografias fictícias dos candidatos

Coloque as imagens PNG nesta pasta. Não inclua fotos de pessoas reais sem
permissão para uso no projeto educativo.

Os nomes esperados pelos cadastros atuais em
`src/main/java/br/edu/urnacidada/service/CatalogoFicticio.java` são:

- `deputado_8101.png` — Lia Inventora
- `deputado_8202.png` — Caio Explorador
- `presidente_81.png` — Nina Aurora
- `presidente_82.png` — Ravi Horizonte

A fotografia é carregada a partir do classpath Maven e exibida na urna
e na apuração. Se o arquivo não existir ou não puder ser aberto, o programa
mostra uma silhueta neutra (sem erro na votação).

Recomendação: fotos em formato retrato, tamanho 300 x 400 px ou maior,
sem textos adicionais.

Para incluir um novo candidato, insira uma `Ficha` na lista `FICHAS`
de `CatalogoFicticio.java` e configure a rota de seu arquivo PNG.
O mesmo cadastro alimenta os dois aplicativos; mantenha números distintos
por cargo e use partidos/legendas fictícias.
