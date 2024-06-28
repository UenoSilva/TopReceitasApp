# Top Receitas App

O Top Receitas App é uma aplicação móvel que permite aos usuários descobrir e gerenciar receitas de culinária. Esta aplicação foi desenvolvida como parte de um projeto de demonstração para mostrar conceitos de desenvolvimento de aplicativos Android usando Kotlin.

## Tecnologia Utilizadas

- Android SDK e Kotlin
- Glide
- Material Components
- SQLite
- Retrofit2
- Gson
- ViewBinding
- ViewPager2


## Recursos Principais

- **Explorar Receitas:** Os usuários podem explorar uma ampla variedade de receitas com base em categorias.
- **Salvar Favoritos:** Os usuários podem salvar suas receitas favoritas para acessá-las facilmente mais tarde.
- **Criar Receitas Personalizadas:** Os usuários podem criar e gerenciar suas próprias receitas personalizadas.

## Funcionalidades

- Visualizar uma lista de receitas.
- Visualizar detalhes de uma receita.
- Adicionar receitas aos favoritos.
- Remover receitas dos favoritos.
- Adicionar novas receitas criadas pelos usuários.
- Visualizar e gerenciar receitas adicionadas pelos usuários.
- Alternar entre modos claro e escuro.

## Estrutura do Banco de Dados Local

O aplicativo utiliza um banco de dados SQLite para armazenar informações sobre receitas favoritas e receitas criadas pelo usuário. Aqui está a estrutura das tabelas no banco de dados:

### Tabela de Receitas Favoritas

| Coluna                | Tipo    | Descrição                          |
|-----------------------|---------|------------------------------------|
| _ID                   | INTEGER | Chave primária autoincrementada    |
| receita_id            | INTEGER | ID da receita                      |
| titulo                | TEXT    | Título da receita                  |
| imagem                | TEXT    | URL da imagem da receita           |
| porcoes               | INTEGER | Número de porções da receita       |
| timer                 | INTEGER | Tempo de preparo da receita (min)  |
| categoria             | TEXT    | Categoria da receita (JSON array)  |
| ingredientes          | TEXT    | Ingredientes da receita (JSON array) |
| preparo               | TEXT    | Modo de preparo da receita (JSON array) |
| dicas                 | TEXT    | Dicas adicionais para a receita    |

### Tabela de Minhas Receitas

| Coluna                | Tipo    | Descrição                          |
|-----------------------|---------|------------------------------------|
| _ID                   | INTEGER | Chave primária autoincrementada    |
| receita_id            | INTEGER | ID da receita                      |
| titulo                | TEXT    | Título da receita                  |
| imagem                | TEXT    | URL da imagem da receita           |
| porcoes               | INTEGER | Número de porções da receita       |
| timer                 | INTEGER | Tempo de preparo da receita (min)  |
| categoria             | TEXT    | Categoria da receita (JSON array)  |
| ingredientes          | TEXT    | Ingredientes da receita (JSON array) |
| preparo               | TEXT    | Modo de preparo da receita (JSON array) |
| dicas                 | TEXT    | Dicas adicionais para a receita    |

Essas tabelas são definidas pelo arquivo `ReceitasContract.kt` no pacote `br.com.topreceitas.data.local`, e cada tabela possui uma classe interna que implementa a interface `BaseColumns`, fornecendo os nomes das colunas e do próprio banco de dados.

## Estrutura do Projeto

```plaintext
receitaapp/
│
├── app/                    # Diretório principal do aplicativo
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/br/com/topreceitas/       # Código Java
│   │   │   │   ├── ui/                        # Pacote de UI
│   │   │   │   │   ├── MainActivity.kt        # Activity principal
│   │   │   │   │   ├── AddReceitaActivity.kt  # Activity para adicionar receitas
│   │   │   │   │   ├── ReceitaDetailsActivity.kt # Activity para visualizar detalhes da receita
│   │   │   │   │   └── fragments/             # Fragmentos
│   │   │   │   │       ├── FavoriteFragment.kt # Fragmento para exibir receitas favoritas
│   │   │   │   │       └── MinhasReceitasFragment.kt # Fragmento para exibir receitas criadas pelo usuário
│   │   │   │   ├── adapter/                   # Adaptadores
│   │   │   │   │   ├── ReceitasAdapter.kt    # Adaptador para a exibição de receitas em uma RecyclerView
│   │   │   │   │   ├── ViewPageAdapter.kt    # Adaptador para gerenciar as abas do ViewPager
│   │   │   │   ├── maneger/                   #
│   │   │   │   │   ├── ReceitasManager.kt     # Gerenciamento de receitas
│   │   │   │   ├── data/                      # Dados locais
│   │   │   │   │   ├── local/                 # Repositórios locais
│   │   │   │   │   │   ├── ReceitasContract   # Contrato do banco de dados
│   │   │   │   │   │   ├── ReceitasDbHelper.kt # Classe helper para manipulação do banco de dados SQLite
│   │   │   │   │   │   ├── ReceitasFavoritasRepository.kt # Repositório para operações de banco de dados relacionadas a receitas favoritas
│   │   │   │   │   │   ├── MinhasReceitasRepository.kt # Repositório para operações de banco de dados relacionadas a receitas criadas pelo usuário
│   │   │   ├── res/                           # Recursos (layouts, strings, etc.)
│   │   │   ├── AndroidManifest.xml            # Manifesto do Android
│   ├── build.gradle                           # Configurações de build
│   └── ...
├── gradle/                 # Configurações do Gradle
├── build.gradle            # Configurações de build do projeto
├── settings.gradle         # Configurações do projeto
└── README.md               # Documentação do projeto
```

## Api

### Simulação da API

O Top Receitas App utiliza o GitHub Pages para simular uma API que fornece os dados das receitas. A URL da API é https://uenosilva.github.io/TopReceitasApp/api/receitas.json.

### Estrutura do JSON

A API retorna os dados das receitas em formato JSON. Aqui está a estrutura do JSON:

```Json
{
  "id": 0,
  "titulo": "Isca de Frango Crocante",
  "imagem": "https://raw.githubusercontent.com/UenoSilva/TopReceitasApp/main/api/images/Isca-de-Frango-Crocante.jpg",
  "porcoes": 4,
  "tempo": 30,
  "categoria": [
    {
      "tipo": "Salgados"
    }
  ],
  "ingredientes": [
    {
      "ingredientes_titulo": "",
      "ingredientes_ingredientes": [
        "500 gramas de peito de frango",
        "1 colher de chá de sal",
        "1 colher de chá de pimenta-do-reino preta em pó",
        "3 de colheres de sopa Maionese Hellmann's Tradicional",
        "1 xícara de chá de farinha de rosca",
        "Óleo para fritar",
        "3 colheres de sopa de Maionese Hellmann's Supreme"
      ]
    }
  ],
  "preparo": [
    {
      "preparo_titulo": "",
      "preparo_modo": [
        "Corte o peito de frango em Iscas.",
        "Tempere as Iscas de Frango com sal e pimenta.",
        "Misture a Maionese Hellmann’s Tradicional no frango, cobrindo bem todas as partes.",
        "Depois empane com a farinha de rosca.",
        "Frite, aos poucos, em imersão, em óleo não muito quente, por 2 minutos de cada lado, ou até dourarem.",
        "Escorra em papel-toalha e sirva acompanhado da Maionese Hellmann’s Supreme."
      ]
    }
  ],
  "dicas": "Se desejar, prepare a isca de frango crocante na airfryer é só adicionar um fio de óleo ou azeite nas iscas, misturar bem e colocar na airfryer a 180ºC de 15 a 20 minutos ou até dourar. Na hora de servir, tragar mais opções de sabores e escolha sua Maionese Hellmann’s Saborizada preferida."
}
```

## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).

## Links e Referências 

- `[Kotlin](https://kotlinlang.org/docs/home.html)`
- `[Developer Android](https://developer.android.com/topic/architecture/intro?hl=pt-br)`
- `[Material2](https://m2.material.io/components?platform=android)`
- `[Material3](https://m3.material.io/components)`
- `[Glide](https://github.com/bumptech/glide)`
- `[Retrofit](https://square.github.io/retrofit/)`
