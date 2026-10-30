# Plano de testes

## Automatizados (JUnit 5)
`mvn clean test`

- Voto nominal para cargo proporcional e majoritário.
- Voto de legenda para deputado federal.
- Votos branco e nulo.
- Número incompleto não pode ser confirmado.
- Tecla CORRIGE limpa a entrada atual.
- Avanço sequencial entre os cargos.
- Sessão concluída somente após os dois cargos.
- Registro em memória idempotente para o mesmo identificador de sessão.
- Rejeição de sessão incompleta e de voto fora de ordem.

## Teste manual com interface
1. Execute `mvn package` e `java -jar target/urna-cidada-0.1.0.jar`.
2. Inicie a simulação.
3. Digite `8101` e confirme. Deve abrir o cargo Presidente.
4. Selecione BRANCO e CONFIRMA. Deve aparecer FIM.
5. Faça uma nova simulação usando CORRIGE, voto de legenda `81` e número inexistente para presidente.
6. Verifique que não aparecem nomes ou dados de pessoas reais.

Os testes automáticos e visuais precisam ser executados em um ambiente com JDK 21 e Maven. Não confundir testes escritos com testes executados.

