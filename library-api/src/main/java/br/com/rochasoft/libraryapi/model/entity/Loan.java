package br.com.rochasoft.libraryapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Loan
{

    private long id;
    private String customer;
    private Book book;
    private LocalDate loanDate;
    private boolean returned;


}