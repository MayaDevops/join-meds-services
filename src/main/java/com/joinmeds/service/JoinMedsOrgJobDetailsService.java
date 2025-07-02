package com.joinmeds.service;


import com.joinmeds.contract.JoinMedsOrgJobDetailsReqDTO;
import com.joinmeds.contract.JoinMedsOrgJobDetailsResDTO;
import com.joinmeds.model.JoinMedsOrgDetails;
import com.joinmeds.model.JoinMedsOrgJobDetails;
import com.joinmeds.respository.JoinMedsOrgDetailsRepository;
import com.joinmeds.respository.JoinMedsOrgJobDetailsRepository;
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
public class JoinMedsOrgJobDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private final JoinMedsOrgJobDetailsRepository joinOrgJobDetailsRepository;

    @Autowired
    private JoinMedsOrgDetailsRepository joinMedsOrgDetailsRepository;

   public UUID save (UUID id, JoinMedsOrgJobDetailsReqDTO request){

       JoinMedsOrgDetails JoinMedsOrgDetails = joinMedsOrgDetailsRepository.findByUserId(id)
               .orElseThrow(() -> new NoSuchElementException("No Organization found with ID: " + id));

       JoinMedsOrgJobDetails joinOrgJobDetails = new JoinMedsOrgJobDetails();

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
       joinOrgJobDetails.setIsActive(true);

       joinOrgJobDetailsRepository.save(joinOrgJobDetails);

       JoinMedsOrgJobDetails savedUser = joinOrgJobDetailsRepository.save(joinOrgJobDetails);
       return savedUser.getId(); // return ID

   }

    public String update (UUID id, JoinMedsOrgJobDetailsReqDTO request){


        JoinMedsOrgJobDetails joinOrgJobDetails = joinOrgJobDetailsRepository.findById(id)
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

    public String delete (UUID id){

        JoinMedsOrgJobDetails joinOrgJobDetails = joinOrgJobDetailsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Job application not found."));

        joinOrgJobDetails.setIsActive(false);

        joinOrgJobDetailsRepository.save(joinOrgJobDetails);

        return "Job application details deleted successfully.";

    }

    public List<JoinMedsOrgJobDetailsResDTO> fetchByUserId(UUID id) {
        List<JoinMedsOrgJobDetails> entities = joinOrgJobDetailsRepository.findByUserId(id);

        if (entities.isEmpty()) {
            throw new NoSuchElementException("Job applications not found with user ID: " + id);
        }

        return entities.stream()
                .filter(job -> Boolean.TRUE.equals(job.getIsActive()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

    private JoinMedsOrgJobDetailsResDTO mapToDTO(JoinMedsOrgJobDetails entity) {

        return JoinMedsOrgJobDetailsResDTO.builder()
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
