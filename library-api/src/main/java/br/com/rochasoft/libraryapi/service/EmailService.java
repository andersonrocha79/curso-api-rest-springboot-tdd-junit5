package br.com.rochasoft.libraryapi.service.EmailService;

import java.util.List;

// servidor de email > https://mailtrap.io/
// logado com a conta do github
public interface EmailService
{

    void sendMails(String mensagem, List<String> mailsList);
}
