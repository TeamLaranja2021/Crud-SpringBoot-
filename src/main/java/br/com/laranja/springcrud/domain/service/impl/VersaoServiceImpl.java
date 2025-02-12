package br.com.laranja.springcrud.domain.service.impl;


import br.com.laranja.springcrud.domain.dto.versao.VersaoRequest;
import br.com.laranja.springcrud.domain.dto.versao.VersaoForm;
import br.com.laranja.springcrud.domain.model.Projeto;
import br.com.laranja.springcrud.domain.model.Versao;
import br.com.laranja.springcrud.domain.service.VersaoService;
import br.com.laranja.springcrud.infrastructure.exception.EntityWithDependentsException;
import br.com.laranja.springcrud.infrastructure.exception.ProjetoNotFoundException;
import br.com.laranja.springcrud.infrastructure.exception.VersaoNotFoundException;
import br.com.laranja.springcrud.infrastructure.repository.ProjetoRepository;
import br.com.laranja.springcrud.infrastructure.repository.VersaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VersaoServiceImpl implements VersaoService {
    private  final VersaoRepository versaoRepository;
    private  final ProjetoRepository projetoRepository;

    @Override
    public List<Versao> getAllVersao() {
        return versaoRepository.findAll();
    }

    @Override
    public Versao getVersaoById(Long idVersao) {
        return versaoRepository.findById(idVersao).orElseThrow( () -> new VersaoNotFoundException(idVersao));
    }

    @Override
    public Versao createVersao(VersaoForm versaoform) {
        Optional<Projeto> OptionalProjeto = projetoRepository.findById(versaoform.getIdProjeto());

        if (OptionalProjeto.isEmpty() ){
            throw  new ProjetoNotFoundException(versaoform.getIdProjeto());
        }

        Versao versao = Versao.builder()
                .gmud(versaoform.getGmud())
                .descricao(versaoform.getDescricao())
                .dataLancamento(versaoform.getDataLancamento())
                .situacao(versaoform.getSituacao())
                .ordem(versaoform.getOrdem())
                .numeroVersao(versaoform.getNumeroVersao())
                .projeto(OptionalProjeto.get())
                .build();
        return versaoRepository.save(versao);
    }

    @Override
    public Versao updateVersaoById(Long idVersao, VersaoRequest versaoRequest) {
      Optional<Versao> VersaoOptional = versaoRepository.findById(idVersao);

        if (!VersaoOptional.isPresent()) {
            throw new VersaoNotFoundException(idVersao);
        }
        Optional<Projeto> OptionalProjeto = projetoRepository.findById(versaoRequest.getIdProjeto());

        if (!OptionalProjeto.isPresent()) {
            throw new ProjetoNotFoundException(versaoRequest.getIdProjeto());
        }
        Projeto projetoExistent = OptionalProjeto.get();


      return versaoRepository.save( Versao.builder()
                .idVersao(VersaoOptional.get().getIdVersao())
                .gmud(versaoRequest.getGmud())
                .descricao(versaoRequest.getDescricao())
                .dataLancamento(versaoRequest.getDataLancamento())
                .situacao(versaoRequest.getSituacao())
                .ordem(versaoRequest.getOrdem())
                .numeroVersao(versaoRequest.getNumeroVersao())
                .projeto(projetoExistent)
                .build());
    }

    @Override
    @Transactional
    public void deleteByIdVersao(Long idVersao) throws EntityWithDependentsException, VersaoNotFoundException {
        Optional<Versao> versao = Optional.ofNullable(this.getVersaoById(idVersao));
        if (!versao.isPresent()){
            throw new VersaoNotFoundException(idVersao);
        }
        Optional<Projeto> OptionalProjeto = projetoRepository.findById(versao.get().getProjeto().getIdProjeto());

        if (OptionalProjeto.isPresent()) {
            throw new EntityWithDependentsException("Versão","projeto");
        }
        versaoRepository.deleteByIdVersao(idVersao);
    }
}
