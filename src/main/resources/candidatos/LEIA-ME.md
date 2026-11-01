# Imagens dos candidatos — Urna Cidadã

A pasta contém imagens usadas em **candidaturas satíricas**, inclusive memes, personagens e referências a pessoas conhecidas. Nenhum personagem ou pessoa retratada é candidato real por causa deste projeto. Confiram a adequação e os direitos de uso das imagens antes de uma apresentação pública.

O arquivo `src/main/java/br/edu/urnacidada/service/CatalogoFicticio.java` associa cada candidatura aos PNGs abaixo:

| Número | Cargo | Nome no simulador | Arquivo |
| --- | --- | --- | --- |
| 67 | Presidente | Coloridinho | `coloridinho.png` |
| 6767 | Deputado federal | Emily Vik | `emily_vik.png` |
| 10 | Presidente | Gato Nihao | `gato_nihao.png` |
| 1069 | Deputado federal | Gato Babao | `Gato_Babao.png` |
| 90 | Presidente | P Diddy | `pdiddy.png` |
| 9022 | Deputado federal | Elon Musk | `elon_musk.png` |
| 66 | Presidente | IT a Coisa | `IT.png` |
| 6666 | Deputado federal | cthulhu | `cutulo.png` |

Os nomes dos arquivos diferenciam maiúsculas de minúsculas em alguns sistemas. A urna e a apuração carregam as fotos localmente pelo classpath do Maven, sem conexão com internet. Se um arquivo estiver ausente, aparece uma silhueta neutra.

Para adicionar outra candidatura, inclua sua `Ficha` em `CatalogoFicticio.java`, defina seu número e partido fictício e coloque o PNG correspondente nesta pasta.
