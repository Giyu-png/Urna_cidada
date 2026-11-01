# Plano de testes — Urna Cidadã

## Testes automatizados

- `mvn clean test`: testes unitários (sem credenciais Firebase).
- `firebase emulators:exec --only firestore --project demo-urna-cidada "mvn clean verify"`: inclui os testes de transação no Firestore Emulator local.
- O workflow `.github/workflows/java-ci.yml` roda a segunda opção no GitHub Actions.

A suíte cobre classificação de votos, voto de legenda, branco, nulo, número incompleto, CORRIGE, sequência de cargos, cadastro centralizado e disponibilidade das fotos dos oito candidatos, agregação de resultados, reenvio sem dupla contagem e rejeição de sessão incompleta.

## Teste manual no computador autorizado

1. Execute `br.edu.urnacidada.Main` (IntelliJ ou JAR empacotado).
2. Digite `6767` (Emily Vik) para deputado federal e confirme.
3. Digite `67` (Coloridinho) para presidente e confirme.
4. Confira sincronização bem-sucedida com o Firestore; não exponha tokens ou credenciais.
5. Execute `br.edu.urnacidada.apuracao.ApuracaoMain`, atualize e confira as duas contagens.
6. Faça outro teste com CORRIGE, voto de legenda `10` para deputado e voto BRANCO ou NULO para presidente.
7. Verifique as fotografias, abas, percentuais, ordenação e comportamento ao redimensionar a janela.
8. Se possível, simule falta de conexão e confirme que a interface oferece reenvio ou continuação sem afirmar que salvou remotamente.

Os dados de testes manuais ficam no Firestore e **não são apagados** pelos testes unitários. Dados registrados com o catálogo antigo (81/82/8101/8202) continuam históricos; não os confunda com o novo elenco satírico.

Registre capturas para o relatório antes de qualquer limpeza do banco.
