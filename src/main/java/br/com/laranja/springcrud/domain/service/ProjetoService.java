package br.com.laranja.springcrud.domain.service;

import br.com.laranja.springcrud.domain.dto.ProjetoRequest;
import br.com.laranja.springcrud.domain.model.Projeto;

import java.util.List;

public interface ProjetoService {

    List<Projeto> getAllProjetos();

    Projeto getProjetoById(Long idProjeto); // listagem de um projetos pelo id

    Projeto createProjeto(ProjetoRequest projetoRequest); // criação dos projetos

    Projeto updateProjetoById(Long idProjeto, ProjetoRequest projetoRequest); // atualização dos projetos pelo id

    void deleteProjeotById(Long idProjeto); // exclusão dos projetos  pelo id
}
