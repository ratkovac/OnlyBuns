package com.group27.OnlyBuns.service;

import com.group27.OnlyBuns.model.Post;
import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.model.UserWeeklyStatistic;
import com.group27.OnlyBuns.repository.LikeRepository;
import com.group27.OnlyBuns.repository.PostRepository;
import com.group27.OnlyBuns.repository.UserRepository;
import com.group27.OnlyBuns.repository.UserWeeklyStatisticRepository;
import com.group27.OnlyBuns.service.EmailSenderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final UserWeeklyStatisticRepository userWeeklyStatisticRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public NotificationService(UserRepository userRepository, EmailSenderService emailSenderService, UserWeeklyStatisticRepository userWeeklyStatisticRepository, PostRepository postRepository, LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.userWeeklyStatisticRepository = userWeeklyStatisticRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    @Transactional
    public void notifyInactiveUsers() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<User> inactiveUsers = userRepository.findInactiveUsersSince(sevenDaysAgo);
        for (User user : inactiveUsers) {
            int postCount = (int) postRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(7));
            int likes = 0;
            for (Post post : postRepository.findByUserId(user.getId())) {
                likes += likeRepository.countByPostIdAndCreatedAtAfter(
                        post.getId(),
                        LocalDateTime.now().minusDays(7)
                );
            }

            UserWeeklyStatistic userWeeklyStatistic = new UserWeeklyStatistic(user.getId(), likes, postCount, 1);
            //userWeeklyStatistic = userWeeklyStatisticRepository.getReferenceById(user.getId());
            userWeeklyStatistic.setPosts(postCount);
            String emailBody = generateEmailContent(user, userWeeklyStatistic);
            System.out.println(emailBody);
            emailSenderService.sendEmail(user.getEmail(), "OnlyBuns: Pogledaj nove objave!", emailBody);
        }
    }

    private String generateEmailContent(User user, UserWeeklyStatistic userWeeklyStatistic) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Zdravo, ").append(user.getFirstName()).append("!\n\n")
                .append("Evo kratkog pregleda tvoje aktivnosti u poslednjih 7 dana:\n")
                .append("- Dobio/la si ").append(userWeeklyStatistic.getLikes()).append(" novih lajkova.\n")
                .append("- ").append(userWeeklyStatistic.getPosts()).append(" novih objava su tvoji pratioci podelili.\n")
                .append("- ").append(userWeeklyStatistic.getFollowers()).append(" novih pratilaca je odlučilo da prati tvoj profil.\n\n")
                .append("Tvoji pratioci čekaju da vidiš šta su novo objavili! Pridruži se OnlyBuns zajednici i budi u toku sa svim dešavanjima.\n\n")
                .append("Srdačno,\n")
                .append("OnlyBuns tim");

        return emailContent.toString();
    }

    public Optional<UserWeeklyStatistic> getUserById(Long userId) {
        return userWeeklyStatisticRepository.findById(userId);
    }

    public List<UserWeeklyStatistic> getAllStatistics() {
        return userWeeklyStatisticRepository.findAll();
    }

    public UserWeeklyStatistic createStatistic(UserWeeklyStatistic statistic) {
        return userWeeklyStatisticRepository.save(statistic);
    }

    public UserWeeklyStatistic updateStatistic(Long id, UserWeeklyStatistic updatedStatistic) {
        return userWeeklyStatisticRepository.findById(id).map(statistic -> {
            statistic.setLikes(updatedStatistic.getLikes());
            statistic.setPosts(updatedStatistic.getPosts());
            statistic.setFollowers(updatedStatistic.getFollowers());
            return userWeeklyStatisticRepository.save(statistic);
        }).orElseThrow(() -> new RuntimeException("Statistic not found for id: " + id));
    }

    public UserWeeklyStatistic patchStatistic(Long id, UserWeeklyStatistic patchData) {
        return userWeeklyStatisticRepository.findById(id).map(statistic -> {
            if (patchData.getLikes() != 0) {
                statistic.setLikes(patchData.getLikes());
            }
            if (patchData.getPosts() != 0) {
                statistic.setPosts(patchData.getPosts());
            }
            if (patchData.getFollowers() != 0) {
                statistic.setFollowers(patchData.getFollowers());
            }
            return userWeeklyStatisticRepository.save(statistic);
        }).orElseThrow(() -> new RuntimeException("Statistic not found for id: " + id));
    }

    public void deleteStatistic(Long id) {
        if (userWeeklyStatisticRepository.existsById(id)) {
            userWeeklyStatisticRepository.deleteById(id);
        } else {
            throw new RuntimeException("Statistic not found for id: " + id);
        }
    }

    public Optional<UserWeeklyStatistic> getStatisticById(Long id) {
        return userWeeklyStatisticRepository.findById(id);
    }
}
