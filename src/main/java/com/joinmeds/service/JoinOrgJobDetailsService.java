package com.joinmeds.service;


import com.joinmeds.contract.JoinOrgJobDetailsReqDTO;
import com.joinmeds.contract.JoinOrgJobDetailsResDTO;
import com.joinmeds.model.JoinMedsOrgDetails;
import com.joinmeds.model.JoinOrgJobDetails;
import com.joinmeds.respository.JoinMedsOrgDetailsRepository;
import com.joinmeds.respository.JoinOrgJobDetailsRepository;
import com.joinmeds.respository.UserDetailsRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class JoinOrgJobDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private final JoinOrgJobDetailsRepository joinOrgJobDetailsRepository;

    @Autowired
    private JoinMedsOrgDetailsRepository joinMedsOrgDetailsRepository;

   public UUID save (UUID id, JoinOrgJobDetailsReqDTO request){

       JoinMedsOrgDetails JoinMedsOrgDetails = joinMedsOrgDetailsRepository.findByUserId(id)
               .orElseThrow(() -> new NoSuchElementException("No Organization found with ID: " + id));

       JoinOrgJobDetails joinOrgJobDetails = new JoinOrgJobDetails();

       joinOrgJobDetails.setUserId(JoinMedsOrgDetails.getUserId());
       joinOrgJobDetails.setOrgId(JoinMedsOrgDetails.getId());
       joinOrgJobDetails.setHiringFor(request.getHiringFor());
       joinOrgJobDetails.setYearExp(request.getYearExp());
       joinOrgJobDetails.setSkills(request.getSkills());
       joinOrgJobDetails.setNatureJob(request.getNatureJob());
       joinOrgJobDetails.setPayFrom(request.getPayFrom());
       joinOrgJobDetails.setPayTo(request.getPayTo());
       joinOrgJobDetails.setPayRange(request.getPayRange());
       joinOrgJobDetails.setJobDesc(request.getJobDesc());

       joinOrgJobDetailsRepository.save(joinOrgJobDetails);

       JoinOrgJobDetails savedUser = joinOrgJobDetailsRepository.save(joinOrgJobDetails);
       return savedUser.getId(); // return ID

   }

    public String update (UUID id, JoinOrgJobDetailsReqDTO request){


        JoinOrgJobDetails joinOrgJobDetails = joinOrgJobDetailsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Job application not found."));

        if (request.getHiringFor() != null) joinOrgJobDetails.setHiringFor(request.getHiringFor());
        if (request.getYearExp() != null) joinOrgJobDetails.setYearExp(request.getYearExp());
        if (request.getSkills() != null) joinOrgJobDetails.setSkills(request.getSkills());
        if (request.getNatureJob() != null) joinOrgJobDetails.setNatureJob(request.getNatureJob());
        if (request.getPayFrom() != null) joinOrgJobDetails.setPayFrom(request.getPayFrom());
        if (request.getPayTo() != null) joinOrgJobDetails.setPayTo(request.getPayTo());
        if (request.getPayRange() != null) joinOrgJobDetails.setPayRange(request.getPayRange());
        if (request.getJobDesc() != null) joinOrgJobDetails.setJobDesc(request.getJobDesc());

        joinOrgJobDetailsRepository.save(joinOrgJobDetails);

        return "Job application details updated successfully.";

    }

    public List<JoinOrgJobDetailsResDTO> fetchByUserId(UUID id) {
        List<JoinOrgJobDetails> entities = joinOrgJobDetailsRepository.findByUserId(id);

        if (entities.isEmpty()) {
            throw new NoSuchElementException("Job applications not found with user ID: " + id);
        }

        return entities.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

    private JoinOrgJobDetailsResDTO mapToDTO(JoinOrgJobDetails entity) {
        return JoinOrgJobDetailsResDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .orgId(entity.getOrgId())
                .hiringFor(entity.getHiringFor())
                .yearExp(entity.getYearExp())
                .skills(entity.getSkills())
                .natureJob(entity.getNatureJob())
                .payFrom(entity.getPayFrom())
                .payTo(entity.getPayTo())
                .payRange(entity.getPayRange())
                .jobDesc(entity.getJobDesc())
                .build();
    }

}
