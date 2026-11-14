# Recursos de imagem — Urna Cidadã

Esta pasta armazena imagens locais das candidaturas utilizadas na simulação educativa. As referências são definidas no arquivo `src/main/java/br/edu/urnacidada/service/CatalogoFicticio.java`.

## Como manter o cadastro

1. Cadastre ou atualize o partido e a candidatura no `CatalogoFicticio.java`.
2. Informe o caminho `/candidatos/nome-do-arquivo.png` na respectiva `Ficha`.
3. Adicione o arquivo PNG correspondente nesta pasta, mantendo o nome e a capitalização exatos.
4. Execute `mvn clean test` e confira se as imagens de todas as candidaturas cadastradas são encontradas.
5. Confira também a apresentação das fotos na urna e no painel de apuração.

A lista de candidaturas **não é fixada nesta documentação** porque pode ser atualizada para atividades educativas diferentes. Alguns arquivos antigos podem permanecer armazenados sem estar referenciados no catálogo atual.

## Uso responsável

As candidaturas são parte de uma simulação, não representam posições políticas nem eleições reais. Antes de apresentar o projeto em uma escola, revisem a adequação ao público, a licença e os direitos de uso de cada imagem.

As imagens são carregadas a partir dos recursos locais do programa, não do Firebase. Quando uma imagem cadastrada não pode ser carregada, a interface apresenta uma silhueta neutra.
