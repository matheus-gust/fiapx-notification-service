package br.com.fiap.fiapx.bdd;

import br.com.fiap.fiapx.notification.application.NotificationService;
import br.com.fiap.fiapx.notification.application.messages.NotificationMessage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationSteps {

    private final JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
    private final NotificationService service = new NotificationService(mailSender);

    private NotificationMessage message;

    {
        ReflectionTestUtils.setField(service, "fromEmail", "noreply@fiapx.com");
    }

    @Given("que existe uma notificacao de erro para {string} com mensagem {string}")
    public void notificacaoDeErro(String email, String errorMsg) {
        message = new NotificationMessage(UUID.randomUUID(), email, errorMsg);
    }

    @When("o servico de notificacao processa a mensagem")
    public void processaMensagem() {
        service.notifyError(message);
    }

    @Then("um email e enviado para {string}")
    public void emailEnviado(String email) {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        assertThat(captor.getValue().getTo()).contains(email);
    }

    @Then("o email contem a mensagem de erro {string}")
    public void emailContemErro(String errorMsg) {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, atLeastOnce()).send(captor.capture());
        assertThat(captor.getValue().getText()).contains(errorMsg);
    }

    @Given("que o servidor de email esta indisponivel")
    public void servidorIndisponivel() {
        message = new NotificationMessage(UUID.randomUUID(), "user@test.com", "erro");
        doThrow(new RuntimeException("SMTP down")).when(mailSender).send(any(SimpleMailMessage.class));
    }

    @When("o servico de notificacao tenta enviar para {string}")
    public void tentaEnviar(String email) {
        message = new NotificationMessage(UUID.randomUUID(), email, "erro");
    }

    @Then("nenhuma excecao e lancada")
    public void nenhumaExcecao() {
        assertThatCode(() -> service.notifyError(message)).doesNotThrowAnyException();
    }
}
