package com.niton.jchad.model;

import com.niton.jchad.ImageService;
import com.niton.jchad.rest.implementation.UserControllerImpl;
import com.niton.jchad.rest.model.ChatMetaData;
import com.niton.jchad.rest.model.ChatResponse;
import com.niton.jchad.verification.ValidName;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Chat implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ID;

	@ValidName
	private String name = "Private Chat";

	@OneToMany(mappedBy = "chat")
	private Set<Member> members;

	@OneToMany(mappedBy = "chat")
	private Set<Message> messages;

	@Size(min = 1024)
	private byte[] encryptionKey;

	@Nullable
	private String pictureId;

	//@ElementCollection
	//private Map<User, LocalDateTime> readStatusSet;


	public boolean isMember(String id) {
		return members.stream().map(i -> i.getUser().getId()).anyMatch(id::equals);
	}

	public Member getMember(String me) {
		return getMembers().stream().filter(m -> m.getUser().getId().equals(me)).findFirst().orElse(null);
	}

	/**
	 * Only for private chats
	 * @param me my id
	 * @return the partner member
	 */
	public Member getPartner(String me){
		return getMembers().stream().filter(m -> !m.getUser().getId().equals(me)).findFirst().orElse(null);
	}

	public ChatResponse createResponeData(String requestor) {
		return new ChatResponse()
				.withId(getID())
				.withName(isPrivateChat() ? getPartner(requestor).getUser().getDisplayName() : getName())
				.withMembers(getMembers()
				              .stream()
				              .map(Member::getUser)
				              .map(User::getUserInformation)
				              .collect(Collectors.toSet()));
	}

	public StreamingResponseBody getImage(ImageService service,String requestor){
		return (outputStream) -> {
			boolean privateChat = isPrivateChat();
			User partner = getPartner(requestor).getUser();
			boolean renderName = privateChat ? partner.getProfilePictureId() == null : getPictureId() == null;
			String nameToRender = privateChat ? partner.getDisplayName() : getName();

			if(renderName)
				ImageIO.write(service.render(nameToRender),"jpeg",outputStream);
			else
				IOUtils.copy(service.getImageInputStream(partner.getProfilePictureId()),outputStream);
		};
	}
	public boolean isPrivateChat(){
		return getMembers().size() == 2;
	}

	public void addMember(User me, boolean admin) {
		Member m = new Member();
		m.setUser(me);
		m.setAdmin(admin);
		members.add(m);
	}

	public ChatMetaData getMetaData() {
		return new ChatMetaData(getMembers().size(),getMessages().size());
	}

	public Message sendMessage(User one, byte[] msg, LocalDateTime sendingTime) {
		Message message = new Message().withChat(this).withSender(one).withText(msg).withSendingTime(sendingTime);
		messages.add(message);
		return message;
	}

	public void answerMessage(
						LocalDateTime messageTime,
                          User originalSender,
                          LocalDateTime answerTime,
                          User me,
                          byte[] msg) {
		sendMessage(me,msg,answerTime).setReferenceMessage(new Message().withChat(this).withSender(originalSender).withSendingTime(messageTime));
	}
}
