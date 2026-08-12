Feature: Notificacao de erros de processamento

  Scenario: Email enviado quando video falha no processamento
    Given que existe uma notificacao de erro para "user@test.com" com mensagem "FFmpeg falhou"
    When o servico de notificacao processa a mensagem
    Then um email e enviado para "user@test.com"
    And o email contem a mensagem de erro "FFmpeg falhou"

  Scenario: Falha no envio de email nao propaga excecao
    Given que o servidor de email esta indisponivel
    When o servico de notificacao tenta enviar para "user@test.com"
    Then nenhuma excecao e lancada
