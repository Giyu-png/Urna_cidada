# Arquitetura — versão 0.1

```text
Swing: JanelaPrincipal, TelaInicial, TelaUrna, TelaFim
   | ações
UrnaController (estado de digitação e sequência)
   | consulta regras                 | ao finalizar sessão
VotacaoService                      VotoRepository (interface)
   |                                   |
model/*                              MemoriaVotoRepository
```

## Separação de responsabilidades
- **model**: Cargo, Partido, Candidato (abstrata), subclasses proporcionais e majoritárias, Voto imutável e SessaoVotacao.
- **service**: valida e classifica números, busca candidaturas e retorna descrições.
- **controller**: mantém dígitos e voto branco em curso, confirma e avança entre os cargos.
- **repository**: contrato de persistência independente da implementação.
- **view**: telas Swing; não executam regras de classificação nem conhecem detalhes de armazenamento.

## Quatro pilares de POO
- **Encapsulamento:** atributos privados e operações controladas em SessaoVotacao e UrnaController.
- **Herança:** CandidatoProporcional e CandidatoMajoritario especializam Candidato.
- **Polimorfismo:** getDescricao() apresenta detalhes diferentes conforme o candidato.
- **Abstração:** VotoRepository separa persistência da regra de negócio.

## Limitações conhecidas
- Apenas dois cargos, votação fictícia e regras reduzidas.
- Dados são mantidos somente enquanto o aplicativo estiver aberto.
- Uma futura integração Firestore deve registrar uma sessão atomicamente e impedir duplicidade.
- Um cliente desktop distribuído **não pode** incorporar credenciais administrativas Firebase.

