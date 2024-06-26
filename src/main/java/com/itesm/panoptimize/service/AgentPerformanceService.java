package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent_performance.*;
import com.itesm.panoptimize.model.AgentPerformance;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.AgentPerformanceRepository;
import com.itesm.panoptimize.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;

@Service
public class AgentPerformanceService {
    private final AgentPerformanceRepository agentPerformanceRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AgentPerformanceService(AgentPerformanceRepository agentPerformanceRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.agentPerformanceRepository = agentPerformanceRepository;
        this.userRepository = userRepository;
    }

    private AgentPerformanceDTO convertToDTO(AgentPerformance agentPerformance) {
        return modelMapper.map(agentPerformance, AgentPerformanceDTO.class);
    }

    private AgentPerformanceMetricsDTO convertToDTO(AgentPerformanceQueryDTO agentPerformance) {
        return modelMapper.map(agentPerformance, AgentPerformanceMetricsDTO.class);
    }

    private AgentPerformance convertDTOToEntity(CreateAgentPerformanceDTO createAgentPerformanceDTO) {
        return modelMapper.map(createAgentPerformanceDTO, AgentPerformance.class);
    }

    public AgentPerformanceDTO getAgentPerformance(Integer id) {
        return convertToDTO(agentPerformanceRepository.findById(id).orElse(null));
    }

    public Page<AgentPerformanceDTO> getAgentPerformances(Pageable pageable) {
        return agentPerformanceRepository.findAll(pageable).map(this::convertToDTO);
    }

    public void deleteAgentPerformance(Integer id) {
        agentPerformanceRepository.deleteById(id);
    }

    public AgentPerformanceDTO createAgentPerformance(CreateAgentPerformanceDTO createAgentPerformanceDTO) {
        AgentPerformance agentPerformanceToCreate = new AgentPerformance();
        agentPerformanceToCreate.setId(null);
        agentPerformanceToCreate.setAvgAfterContactWorkTime(createAgentPerformanceDTO.getAvgAfterContactWorkTime());
        agentPerformanceToCreate.setAvgAbandonTime(createAgentPerformanceDTO.getAvgAbandonTime());
        agentPerformanceToCreate.setAvgHandleTime(createAgentPerformanceDTO.getAvgHandleTime());
        agentPerformanceToCreate.setAvgHoldTime(createAgentPerformanceDTO.getAvgHoldTime());

        User agent = userRepository.findById(createAgentPerformanceDTO.getId()).orElse(null);
        if(agent == null) {throw new IllegalArgumentException("Agent not found");}
        agentPerformanceToCreate.setAgent(agent);

        return convertToDTO(agentPerformanceRepository.save(agentPerformanceToCreate));
    }

    public AgentPerformanceDTO updateAgentPerformance(Integer id, UpdateAgentPerformanceDTO updateAgentPerformanceDTO) {
        AgentPerformance agentPerformance = agentPerformanceRepository.findById(id).orElse(null);
        if (agentPerformance == null) {
            return null;
        }
        if (updateAgentPerformanceDTO.getAgentId() != null) {
            agentPerformance.setAgent(userRepository.findById(updateAgentPerformanceDTO.getAgentId()).orElse(null));
        }
        if (updateAgentPerformanceDTO.getAvgAbandonTime() != null) {
            agentPerformance.setAvgAbandonTime(updateAgentPerformanceDTO.getAvgAbandonTime());
        }
        if (updateAgentPerformanceDTO.getAvgAfterContactWorkTime() != null) {
            agentPerformance.setAvgAfterContactWorkTime(updateAgentPerformanceDTO.getAvgAfterContactWorkTime());
        }
        if(updateAgentPerformanceDTO.getAvgHandleTime() != null) {
            agentPerformance.setAvgHandleTime(updateAgentPerformanceDTO.getAvgHandleTime());
        }
        if(updateAgentPerformanceDTO.getAvgHoldTime() != null) {
            agentPerformance.setAvgHoldTime(updateAgentPerformanceDTO.getAvgHoldTime());
        }

        return convertToDTO(agentPerformanceRepository.save(agentPerformance));
    }

    public AgentPerformanceDTO getAgentPerformanceByNote(Integer id){
        return convertToDTO(agentPerformanceRepository.findAgentPerformanceByNoteId(id));
    }

    public AgentPerformanceMetricsDTO getAgentMetricsToday(Integer agentId){
        AgentPerformanceQueryDTO agentPerformance = agentPerformanceRepository.findAgentMetricsByAgentId(agentId);

        return convertToDTO(agentPerformance);
    }
}
