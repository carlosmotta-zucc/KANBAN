# Projeto Kanban — Sistema de Agendamento de Salas e Laboratórios

> Aplicação **Java de console** desenvolvida em equipe, organizada por **cards (estórias)** em um quadro Kanban.
> Cada desenvolvedor trabalha em uma **branch exclusiva** contendo **somente a tarefa que lhe foi atribuída**.

---

## 1. Contexto

A universidade precisa substituir as planilhas e e-mails que hoje gerenciam o uso dos espaços acadêmicos. A equipe foi contratada para construir um sistema que permita:

- Professores reservarem salas de aula e laboratórios com antecedência.
- Alunos consultarem disponibilidade para estudo em grupo.
- A coordenação visualizar conflitos e a taxa de ocupação por bloco e horário.
- Administradores cadastrarem espaços, equipamentos e regras de uso.

Nesta versão o sistema é entregue como **aplicação de console em Java** (menus interativos no terminal), sem interface web.

---

## 2. Regras gerais da atividade

1. **Trabalho por cards** — todo o desenvolvimento é fatiado nas 12 estórias (KAN-01 a KAN-12). Nada é codificado fora de um card.
2. **Uma branch por card** — cada card vira uma branch. O desenvolvedor responsável trabalha **exclusivamente** na tarefa daquela branch; não mistura código de outra estória.
3. **O dev DEVE informar o card em que está trabalhando** — antes de iniciar, o desenvolvedor declara publicamente qual card assumiu (move o card para "Em Andamento" com seu nome, avisa na daily e cria a branch `feature/KAN-XX-...`). Ninguém codifica um card sem antes registrar que é o responsável por ele.
4. **Dependências devem ser respeitadas** — uma estória só pode ser iniciada quando suas predecessoras estiverem concluídas (ver seção 5).
5. **Movimento só da esquerda para a direita** no quadro: `Backlog → A Fazer → Em Andamento → Revisão → Pronto`.
6. **Limite de WIP é inviolável** — `A Fazer: 3 · Em Andamento: 2 · Revisão: 2`.
7. **"Pronto" exige todos os critérios de aceite atendidos** e revisão (QA) aprovada.
8. **Bloqueios são registrados** — quando um card trava, registra-se o motivo (issue/comentário no card) e ele não volta para o Backlog.

---

## 3. Papéis da equipe

| Papel                     | Responsabilidade primária                                                                     |
| ------------------------- | --------------------------------------------------------------------------------------------- |
| **Product Owner**         | Prioriza o backlog, esclarece critérios de aceite, decide o que entra em "Pronto".            |
| **Líder técnico**         | Garante coerência técnica, gere dependências, valida a ordem de execução e o merge na `main`. |
| **Desenvolvedores (2–3)** | Implementam os cards, fazem o código avançar, registram bloqueios.                            |
| **QA / Revisor**          | Valida critérios de aceite (revisa o Pull Request) antes de mover para "Pronto".              |
| **Facilitador**           | Zela pelo método: WIP, políticas e ritmo (dailies curtas).                                    |

---

## 4. Fluxo de trabalho com Git (card → branch → merge)

### Branch principal

- `main` — sempre estável. Só recebe código revisado e aprovado pelo QA.

### Padrão de branch por card

```
feature/KAN-01-visualizar-salas-disponiveis
feature/KAN-02-reservar-laboratorio
...
```

### Passo a passo para cada desenvolvedor

```bash
# 1. Atualizar a main local
git checkout main
git pull origin main

# 2. Criar a branch EXCLUSIVA do seu card
git checkout -b feature/KAN-08-autenticar-no-sistema

# 3. Trabalhar APENAS na tarefa do card
#    (commits pequenos e descritivos)
git add .
git commit -m "KAN-08: implementa autenticação com perfis de usuário"

# 4. Enviar a branch
git push -u origin feature/KAN-08-autenticar-no-sistema

# 5. Abrir Pull Request da branch -> main
#    O QA revisa os critérios de aceite. Aprovado => merge.
```

### Regras de branch

- **Um card = uma branch = um responsável.** Não comitar código de outro card na sua branch.
- O nome da branch sempre começa com o código do card (`KAN-XX`).
- As mensagens de commit começam com o código do card (`KAN-XX: ...`).
- O merge na `main` só acontece após aprovação do QA (card em "Pronto").
- Respeite a dependência: só comece a branch quando as predecessoras já estiverem mergeadas.

---

## 5. Mapa de dependências

Ordem segura de execução (quem não depende de ninguém vem primeiro):

```
KAN-03  ─┬─> KAN-01
         ├─> KAN-02 ─┬─> KAN-04
         │           ├─> KAN-05
         │           ├─> KAN-06 ──> KAN-10
         │           ├─> KAN-07
         │           ├─> KAN-09
         │           └─> KAN-11
         └─> KAN-11

KAN-08  ─┬─> KAN-02
         ├─> KAN-05
         ├─> KAN-09
         └─> KAN-10

KAN-12  (independente)
```

- **Sem dependências (podem começar já):** KAN-03, KAN-08, KAN-12.
- **Gargalos centrais:** KAN-02 (libera 5 estórias) e KAN-08 (libera 4). Priorizar.

---

## 6. Backlog — as 12 estórias (cards)

> Cada card já traz: ator, critério de aceite, dependências, prioridade e tamanho estimado.

### KAN-01 · Visualizar salas disponíveis

- **Estória:** Como aluno, quero visualizar salas disponíveis por dia e horário, para reservar um espaço de estudo.
- **Critério de aceite:** Listar sala, capacidade, horário e status.
- **Dependências:** KAN-03
- **Desbloqueia:** nenhuma diretamente
- **Prioridade:** Alta · **Tamanho:** M

### KAN-02 · Reservar laboratório para aula prática

- **Estória:** Como professor, quero reservar laboratório para aula prática, para planejar minhas disciplinas.
- **Critério de aceite:** Selecionar laboratório, data, horário e turma.
- **Dependências:** KAN-03, KAN-08
- **Desbloqueia:** KAN-04, KAN-05, KAN-06, KAN-09, KAN-10
- **Prioridade:** Alta · **Tamanho:** M

### KAN-03 · Cadastrar salas e laboratórios

- **Estória:** Como administrador, quero cadastrar salas e laboratórios, para manter a base atualizada.
- **Critério de aceite:** Criar, editar e desativar ambientes.
- **Dependências:** nenhuma
- **Desbloqueia:** KAN-01, KAN-02, KAN-11
- **Prioridade:** Alta · **Tamanho:** M

### KAN-04 · Confirmar reserva

- **Estória:** Como usuário, quero receber confirmação de reserva, para saber se a solicitação foi aceita.
- **Critério de aceite:** Confirmação na tela e registro.
- **Dependências:** KAN-02
- **Desbloqueia:** nenhuma diretamente
- **Prioridade:** Média · **Tamanho:** P

### KAN-05 · Cancelar reserva

- **Estória:** Como usuário, quero cancelar uma reserva, para liberar o ambiente quando necessário.
- **Critério de aceite:** Atualizar disponibilidade após cancelamento.
- **Dependências:** KAN-02, KAN-08
- **Desbloqueia:** nenhuma diretamente
- **Prioridade:** Média · **Tamanho:** P

### KAN-06 · Evitar choque de horários

- **Estória:** Como administrador, quero evitar choque de horários, para impedir reservas duplicadas.
- **Critério de aceite:** Sistema não aprova conflito de horário.
- **Dependências:** KAN-02
- **Desbloqueia:** KAN-10
- **Prioridade:** Alta · **Tamanho:** G

### KAN-07 · Consultar histórico de reservas

- **Estória:** Como coordenador, quero consultar histórico de reservas, para acompanhar uso dos ambientes.
- **Critério de aceite:** Filtrar por período e por ambiente.
- **Dependências:** KAN-02
- **Desbloqueia:** nenhuma diretamente
- **Prioridade:** Baixa · **Tamanho:** M

### KAN-08 · Autenticar no sistema

- **Estória:** Como usuário, quero autenticar no sistema, para acessar apenas minhas funcionalidades.
- **Critério de aceite:** Login diferencia aluno, professor e administrador.
- **Dependências:** nenhuma
- **Desbloqueia:** KAN-02, KAN-05, KAN-09, KAN-10
- **Prioridade:** Alta · **Tamanho:** G

### KAN-09 · Ver reservas futuras

- **Estória:** Como usuário, quero ver minhas reservas futuras, para organizar minha agenda.
- **Critério de aceite:** Exibir reservas ordenadas por data.
- **Dependências:** KAN-02, KAN-08
- **Desbloqueia:** nenhuma diretamente
- **Prioridade:** Média · **Tamanho:** P

### KAN-10 · Aprovar reservas especiais

- **Estória:** Como administrador, quero aprovar reservas especiais, para lidar com pedidos fora do padrão.
- **Critério de aceite:** Status pendente, aprovado e recusado.
- **Dependências:** KAN-02, KAN-06, KAN-08
- **Desbloqueia:** nenhuma diretamente
- **Prioridade:** Média · **Tamanho:** M

### KAN-11 · Painel simples de ocupação

- **Estória:** Como gestor, quero um painel simples de ocupação, para analisar demanda dos espaços.
- **Critério de aceite:** Mostrar ao menos taxa de ocupação por ambiente.
- **Dependências:** KAN-02, KAN-03
- **Desbloqueia:** nenhuma diretamente
- **Prioridade:** Baixa · **Tamanho:** G

### KAN-12 · Exibir erros de preenchimento

- **Estória:** Como usuário, quero receber aviso de erro claro quando faltar algum dado, para corrigir a solicitação.
- **Critério de aceite:** Mensagens indicam o campo com problema.
- **Dependências:** nenhuma
- **Desbloqueia:** nenhuma diretamente
- **Prioridade:** Média · **Tamanho:** P

---

## 7. Quadro Kanban

| Backlog (WIP ∞)              | A Fazer (WIP 3)    | Em Andamento (WIP 2)             | Revisão (WIP 2)       | Pronto             |
| ---------------------------- | ------------------ | -------------------------------- | --------------------- | ------------------ |
| Estórias priorizadas pelo PO | Prontas para puxar | Sendo trabalhadas (branch ativa) | PR em revisão pelo QA | Mergeado na `main` |

- Cada card mostra: código (KAN-XX), título, responsável e status.
- Card bloqueado recebe etiqueta vermelha 🚫 + motivo curto.

---

## 8. Definição de "Pronto" (Definition of Done)

Um card só vai para "Pronto" quando:

1. O código está na branch `feature/KAN-XX-...` e compila sem erros.
2. **Todos os critérios de aceite do card foram atendidos.**
3. Funciona no console (testado manualmente pela equipe).
4. O Pull Request foi **revisado e aprovado pelo QA**.
5. Foi feito o **merge na `main`** sem conflitos.
6. As dependências da estória já estavam em "Pronto" antes de iniciar.

---

## 9. Sugestão de estrutura do projeto Java

```
src/
 └── br/unoesc/agendamento/
      ├── Main.java                 # menu principal do console
      ├── model/                    # Sala, Laboratorio, Reserva, Usuario...
      ├── service/                  # regras de negócio por estória
      ├── repository/               # armazenamento (lista em memória ou arquivo)
      └── util/                     # validações, mensagens de erro (KAN-12)
```

- Sistema de **menu por terminal** (`Scanner`) com opções por perfil de usuário.
- Persistência pode ser em memória (coleções) ou arquivo, conforme combinado pela equipe.
