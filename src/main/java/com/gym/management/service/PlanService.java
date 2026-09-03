package com.gym.management.service;

import com.gym.management.dto.request.PlanCreateRequest;
import com.gym.management.dto.response.PlanResponse;
import com.gym.management.entity.Plan;
import com.gym.management.mapper.PlanMapper;
import com.gym.management.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    @Transactional
    public PlanResponse createPlan(PlanCreateRequest request) {
        if (planRepository.existsByTitle(request.title())) {
            throw new IllegalArgumentException(
                    "A plan with this title already exists."
            );
        }

        Plan plan = planMapper.toEntity(request);
        Plan savedPlan = planRepository.save(plan);

        return planMapper.toResponse(savedPlan);
    }

    public PlanResponse getPlanById(Long id) {
        return planMapper.toResponse(planRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + id)));
    }

    public List<PlanResponse> getAllPlans() {
        return planRepository.findAll()
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }

    private Plan getPlanEntityById(Long id) {
        return planRepository.findById(id).orElse(null);
    }

    @Transactional
    public PlanResponse updatePlan(Long id, PlanCreateRequest request) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + id));

        if (!plan.getTitle().equals(request.title())
                && planRepository.existsByTitle(request.title())) {
            throw new IllegalArgumentException(
                    "A plan with this title already exists."
            );
        }

        plan.setTitle(request.title());
        plan.setPrice(request.price());
        plan.setDurationDays(request.durationDays());
        plan.setTotalSessions(request.totalSessions());
        plan.setPlanType(request.planType());

        return planMapper.toResponse(plan);
    }

    @Transactional
    public void deletePlan(Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + id));
        planRepository.delete(plan);
    }

}
