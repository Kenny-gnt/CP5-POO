# Checkpoint 5 — Bug Hunt PetFiap

> Projeto desenvolvido para a disciplina de Programação Orientada a Objetos (POO), com foco em depuração, exceções, JDBC/DAO, Spring Boot, padrões de projeto, Clean Code e testes unitários.

## Identificação

**Grupo:** 10

| Integrante | RM | Turma |
|---|---|---|
| Ana Luiza | RM563171 | 2CCPG |
| Anny Elly| RM565055 | 2CCPG |
| Gisleine | RM563804 | 2CCPG |
| Larissa | RM564168 | 2CCPG |
| Raira | RM564850 | 2CCPG |
| Sofia | Rm562767 | 2CCPG |


| Campo | Resultado |
|---|---|
| **Total de bugs corrigidos** | **12 / 12** |
| **Total de ajustes de Clean Code** | **6 / 6** |
| **Total de testes novos escritos** | **6 / 6** |
| **Suíte final** | **26 testes, 0 falhas** |

> **Observação:** os arquivos entregues nesta pasta já continham as correções e os três testes novos realizados anteriormente pelo grupo. Como não foi fornecido o histórico Git dos commits anteriores, a numeração e as descrições de BUG01–BUG11 abaixo foram reconstruídas a partir das diferenças entre o projeto original recebido e o estado atual. Se o grupo já possui uma descrição/ordem própria desses commits, mantenha a numeração usada no Git.

---

## Parte 1 — Bugs encontrados

| # | Sintoma observado | Causa raiz | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 | O Builder recebia um nome de pet, mas o valor não era armazenado no atributo do objeto. | `AtendimentoBuilder.comPet()` fazia uma atribuição para o próprio parâmetro (`petNome = petNome`). | Uso de `this.petNome = petNome`. | POO / encapsulamento / Builder |
| bug02 | O Builder permitia criar atendimento sem nome do pet. | Não havia validação de campo obrigatório em `construir()`. | Validação de `null` e vazio para `petNome`, lançando `IllegalArgumentException`. | Builder / exceções |
| bug03 | A montagem sem porte não era rejeitada conforme o contrato. | O Builder não validava `petPorte`. | Validação de `petPorte` antes de chamar a Factory. | Validação / exceções |
| bug04 | Ao solicitar TOSA, a Factory criava um objeto de BANHO. | O `case "TOSA"` instanciava `Banho`. | O `case` passou a instanciar `Tosa`. | Factory / polimorfismo |
| bug05 | O preço de BANHO para porte pequeno estava errado e o preço de porte grande também ficava invertido. | Valores `60.0` e `100.0` estavam associados aos portes errados. | Ajuste da tabela de preços: PEQUENO = 60, MÉDIO = 80, GRANDE = 100. | POO / regra de negócio |
| bug06 | A consulta veterinária não carregava protocolo, pet, porte, tutor e data/hora enviados pelo construtor. | O construtor chamava `super()` sem repassar os parâmetros. | Chamada de `super(protocolo, petNome, petPorte, tutorNome, dataHora)`. | Herança / construtores |
| bug07 | `getDuracaoMinutos()` da TOSA não sobrescrevia o método da classe base. | A assinatura tinha um parâmetro `String porte`, criando sobrecarga em vez de sobrescrita. | Assinatura alterada para `getDuracaoMinutos()` e adicionado `@Override`. | Polimorfismo / override x overload |
| bug08 | Dois atendimentos do mesmo pet no mesmo horário podiam ser agendados. | A comparação usava `==` para objetos `String` e `LocalDateTime`. | Comparação por valor com `equals`/`Objects.equals` no conflito de horário. | `==` x `.equals()` |
| bug09 | Uma busca por ID inexistente podia retornar `null` em vez da exceção definida pelo contrato. | `buscarPorId()` capturava uma exceção genérica e retornava `null`. | Remoção do `catch` genérico e uso de `orElseThrow(AtendimentoNaoEncontradoException)`. | Exceções / Optional |
| bug10 | Cancelar atendimento concluído era aceito, contrariando a máquina de estados. | `cancelar()` alterava o status sem verificar o estado atual. | Cancelamento permitido somente quando o status é `AGENDADO`. | Máquina de estados / exceções |
| bug11 | O Singleton não preservava corretamente a mesma instância entre chamadas de `getInstancia()`. | Quando `instancia` era `null`, o método retornava um novo objeto sem armazená-lo no atributo estático. | A instância criada passou a ser atribuída a `instancia` antes de ser retornada. | Singleton |
| bug12 | Um atendimento com data/hora no passado podia chegar à consulta do repositório e ser salvo. | `AgendaService.agendar()` não validava a data antes de consultar o banco. | Validação no início do método: data/hora nula ou anterior ao momento atual gera `IllegalArgumentException`; o repositório não é consultado. | Regra de negócio / exceções / serviço |

### BUG12 — detalhe da correção

A regra exige que uma tentativa de agendamento no passado seja rejeitada antes de qualquer consulta ao banco. Por isso a validação foi colocada **antes** de `repository.findByPetNome(...)`. O teste também verifica `never().findByPetNome(...)` e `never().save(...)`, protegendo o requisito de que o banco nem seja consultado.

---

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Princípio / boa prática | O que foi mudado |
|---|---|---|---|
| clean01 | `AtendimentoFactory.criar()` | Nomes significativos | Parâmetros de uma letra (`p`, `t`, `n`, `po`, `tu`, `d`) foram renomeados para `protocolo`, `tipo`, `petNome`, `petPorte`, `tutorNome` e `dataHora`. |
| clean02 | `AgendaService` | Injeção de dependência explícita / dependências imutáveis | `@Autowired` em atributo foi substituído por construtor e o repository passou a ser `final`. |
| clean03 | `AtendimentoController` | Injeção de dependência explícita / testabilidade | `@Autowired` em atributo foi substituído por construtor e o service passou a ser `final`. |
| clean04 | `AgendaService` | Evitar saída direta no console | Removido `System.out.println` usado como recibo; a regra de negócio não deve depender de saída no console. |
| clean05 | `GeradorProtocolo` | Evitar efeitos colaterais desnecessários | Removido `System.out.println` executado na criação do Singleton. |
| clean06 | `Atendimento` e `AgendaService` | Evitar magic strings | Criadas as constantes `STATUS_AGENDADO`, `STATUS_CONCLUIDO` e `STATUS_CANCELADO`, usadas nas transições e validações de status. |

---

## Parte 3 — Testes novos

| # | Teste escrito | Regra coberta | Resultado ao escrever |
|---|---|---|---|
| teste01 | `BanhoTest.deveCalcularPrecoDeAcordoComOPorte()` | BANHO deve custar R$ 60, R$ 80 e R$ 100 conforme o porte. | **Vermelho → revelou bug no preço; corrigido e ficou verde.** |
| teste02 | `TosaTest.deveDurar60Minutos()` | TOSA deve durar 60 minutos. | **Vermelho → revelou bug de sobrescrita; corrigido e ficou verde.** |
| teste03 | `AgendaServiceTest.deveRecusarCancelamentoDeAtendimentoJaConcluido()` | Atendimento CONCLUIDO não pode ser cancelado. | **Vermelho → revelou bug na transição de status; corrigido e ficou verde.** |
| teste04 | `AgendaServiceTest.deveRecusarAgendamentoNoPassadoSemConsultarRepositorio()` | Agendamento no passado deve gerar `IllegalArgumentException` antes de consultar/salvar no banco. | **Vermelho → revelou BUG12; corrigido e ficou verde.** |
| teste05 | `ConsultaVeterinariaTest.deveCustar150ReaisIndependentementeDoPorte()` | CONSULTA deve custar R$ 150, independentemente do porte. | **Verde de cara — a regra já estava correta.** |
| teste06 | `AgendaServiceTest.deveCancelarAtendimentoAgendado()` | Atendimento AGENDADO pode ser cancelado e deve ficar com status CANCELADO. | **Verde de cara — a regra já estava correta.** |

---

## Parte 4 — Perguntas de reflexão

### 1. A suíte como contrato (Aula 15)

A suíte foi usada como uma especificação executável do comportamento esperado do PetFiap. Em vez de alterar os testes para combinar com o código, analisamos a diferença entre o valor esperado e o valor produzido para localizar a causa raiz. Um exemplo foi o conflito de horário: o teste criava dois `LocalDateTime` com o mesmo valor e o código usava `==`, fazendo o conflito passar. Outro exemplo foi `buscarPorId()`, que deveria lançar `AtendimentoNaoEncontradoException`, mas devolvia `null`. A vantagem sobre testar somente com `curl` é que os testes são rápidos, repetíveis e isolam model, Builder, Factory e service sem depender do Oracle ou da aplicação inteira.

### 2. Mock e injeção de dependência (Aulas 13 a 15)

No `AgendaServiceTest`, o Mockito cria um `AtendimentoRepository` falso com `@Mock`. O `@InjectMocks` coloca esse objeto falso no `AgendaService`, permitindo testar as regras do serviço sem banco. Em produção, quem resolve a dependência é o container do Spring, que cria o repository real e o entrega ao service por injeção de dependência. Na versão refatorada, o `AgendaService` recebe o repository pelo construtor, deixando a dependência explícita. Assim, no teste, o Mockito fornece o mock; em produção, o Spring fornece o bean real. O resultado é um teste unitário rápido, sem Oracle e sem subir o contexto completo do Spring.

### 3. `==` vs `.equals()` (Aula 7)

O bug de conflito de horário estava em `AgendaService.agendar()`. O código comparava `a.getPetNome() == novo.getPetNome()` e `a.getDataHora() == novo.getDataHora()`. Em Java, `==` compara referências de objetos, e não o conteúdo. Por isso dois objetos diferentes contendo `"Rex"` ou a mesma data/hora podem ser considerados diferentes. Com literais de String, a comparação pode funcionar por causa do pool de Strings, dando a impressão de que o código está correto em alguns casos. A correção passou a comparar os valores. Dessa forma, objetos diferentes com o mesmo conteúdo são reconhecidos como iguais e o conflito é recusado.

### 4. Sobrescrita vs sobrecarga (Aula 7)

Na `Tosa`, o método deveria sobrescrever `getDuracaoMinutos()` definido em `Atendimento`, mas estava declarado como `getDuracaoMinutos(String porte)`. Isso não é override: é outro método, com assinatura diferente, portanto é overload. Quando o sistema chamava `getDuracaoMinutos()` por uma referência `Atendimento`, a implementação da classe base era usada e a TOSA acabava com a duração errada. A correção removeu o parâmetro e adicionou `@Override`. A anotação é importante porque o compilador passa a verificar se realmente existe um método na classe pai com a mesma assinatura, evitando esse tipo de erro silencioso.

### 5. Singleton manual vs bean do Spring (Aula 14)

O `GeradorProtocolo` usa Singleton manual para garantir uma única instância responsável pela numeração global dos protocolos. O bug estava no `getInstancia()`: quando não existia instância, o método criava um objeto e o retornava sem guardar esse objeto no atributo `instancia`. Assim, uma chamada seguinte poderia criar outro gerador e reiniciar a sequência. A correção guarda a instância criada no atributo estático. Já o `AgendaService` é um `@Service`; em produção, o container do Spring administra sua instância e suas dependências. Além disso, a dependência do repository agora está explícita no construtor, o que facilita testes e manutenção.

### 6. Cobertura de testes: onde parar? (Aula 15)

Os seis testes novos são importantes mesmo quando passam de primeira. Um teste verde de imediato, como o de preço fixo da consulta, comprova que a regra já estava correta e cria uma proteção contra regressões futuras. Já os testes que começaram vermelhos mostraram comportamentos que ainda não cumpriam o contrato, como o agendamento no passado. Em um projeto real, eu priorizaria primeiro os caminhos críticos de negócio, depois os principais caminhos de erro e, em seguida, aumentaria a cobertura das áreas de maior risco. Cobertura de 100% pode ser útil, mas não garante sozinha que os testes sejam bons. O mais importante é cobrir as regras que podem causar impacto real e testar também situações inválidas.

---

## Parte 5 — Como executar

### Requisitos

- JDK 17 ou superior
- Maven
- Eclipse ou outra IDE compatível com projetos Maven

### Testes unitários

Na raiz do projeto:

```bash
mvn test
```

Ou no Eclipse:

**src/test/java → botão direito → Run As → JUnit Test**

Os testes são unitários e utilizam Mockito no `AgendaServiceTest`, portanto não dependem do Oracle.

### API

Para executar a API, configure localmente as credenciais do Oracle em `src/main/resources/application.properties`. Não publique RM ou senha reais no GitHub.

---

## Reflexão final

O principal aprendizado do checkpoint foi que um código pode compilar e ainda estar incorreto em regras importantes de negócio. A suíte de testes ajudou a transformar o comportamento esperado em critérios objetivos, enquanto os testes novos permitiram descobrir regras que não estavam protegidas. A atividade também mostrou a diferença entre corrigir um sintoma e corrigir a causa raiz: exemplos como `==` em objetos, sobrescrita incorreta e exceções engolidas só ficam realmente resolvidos quando a estrutura do código é corrigida. Os ajustes de Clean Code complementam a correção funcional porque tornam as dependências, nomes e regras mais claros para quem precisar manter o sistema depois.
