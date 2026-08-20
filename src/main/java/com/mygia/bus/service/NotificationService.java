package com.mygia.bus.service;

import com.mygia.bus.domain.Reservation;
import com.mygia.bus.domain.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final EmailService emailService;
    private final WhatsAppService whatsAppService;

    public NotificationService(EmailService emailService, WhatsAppService whatsAppService) {
        this.emailService = emailService;
        this.whatsAppService = whatsAppService;
    }

    @Async
    public void notifyRegistration(User user) {
        String subject = "Welcome to MyGia Bus Reservation";
        String emailBody = """
                Hello %s,

                Your MyGia account has been created successfully.
                You can now search routes and reserve seats on mygia.lk.

                Safe travels,
                MyGia Team
                """.formatted(user.getFullName());
        String waBody = "Welcome to MyGia, %s! Your account is ready. Book seats anytime from the MyGia portal."
                .formatted(user.getFullName());
        emailService.send(user.getEmail(), subject, emailBody);
        whatsAppService.send(user.getPhoneNumber(), waBody);
    }

    @Async
    public void notifyReservationSuccess(User user, List<Reservation> tickets) {
        String seats = tickets.stream().map(Reservation::getSeatNumber).collect(Collectors.joining(", "));
        Reservation first = tickets.get(0);
        String subject = "MyGia ticket confirmation";
        String emailBody = """
                Hello %s,

                Your reservation is confirmed.
                Route: %s → %s
                Bus: %s
                Departure: %s
                Seats: %s
                Status: CONFIRMED

                Please arrive 20 minutes early. Have a pleasant journey.
                MyGia Team
                """.formatted(
                user.getFullName(),
                first.getRoute().getOrigin().getName(),
                first.getRoute().getDestination().getName(),
                first.getRoute().getBus().getBusNumber(),
                first.getRoute().getDepartureTime(),
                seats);
        String waBody = "MyGia: ticket confirmed. %s → %s on %s, seats %s."
                .formatted(first.getRoute().getOrigin().getName(),
                        first.getRoute().getDestination().getName(),
                        first.getRoute().getDepartureTime(),
                        seats);
        emailService.send(user.getEmail(), subject, emailBody);
        whatsAppService.send(user.getPhoneNumber(), waBody);
    }

    @Async
    public void notifyReservationFailure(User user, String reason) {
        String subject = "MyGia reservation could not be completed";
        String emailBody = """
                Hello %s,

                We could not complete your seat reservation.
                Reason: %s

                The selected seat may have been taken by another customer. Please try again.
                MyGia Team
                """.formatted(user.getFullName(), reason);
        String waBody = "MyGia: reservation failed. %s Please pick another seat."
                .formatted(reason);
        emailService.send(user.getEmail(), subject, emailBody);
        whatsAppService.send(user.getPhoneNumber(), waBody);
    }
}
