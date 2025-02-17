package com.dinhlap.ims.services;

import com.dinhlap.ims.dtos.interviewschedule.ScheduleCreateDTO;
import com.dinhlap.ims.dtos.interviewschedule.ScheduleDetailDTO;
import com.dinhlap.ims.dtos.interviewschedule.ScheduleEditDTO;
import com.dinhlap.ims.dtos.interviewschedule.ScheduleListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewScheduleService {

    Page<ScheduleListDTO> findAll(String search, Long interviewerId , String status, Pageable pageable);

    ScheduleCreateDTO save(ScheduleCreateDTO scheduleCreateDTO);

    ScheduleDetailDTO getScheduleDetail(Long id);

    ScheduleEditDTO getScheduleEdit(Long id);

    String submitResult(Long id, String result, String notes);

    String update(ScheduleEditDTO scheduleEditDTO);

    String cancelSchedule(Long id);

    void sendReminder(Long id, String url);

    void autoSendReminder();
}
