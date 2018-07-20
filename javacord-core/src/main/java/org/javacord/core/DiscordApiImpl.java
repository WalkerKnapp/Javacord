package org.javacord.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.Javacord;
import org.javacord.api.entity.ApplicationInfo;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.message.UncachedMessageUtil;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.group.GroupChannelChangeNameListener;
import org.javacord.api.listener.channel.group.GroupChannelCreateListener;
import org.javacord.api.listener.channel.group.GroupChannelDeleteListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeNameListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeNsfwFlagListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.api.listener.channel.server.ServerChannelChangePositionListener;
import org.javacord.api.listener.channel.server.ServerChannelCreateListener;
import org.javacord.api.listener.channel.server.ServerChannelDeleteListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelChangeTopicListener;
import org.javacord.api.listener.channel.server.text.WebhooksUpdateListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeBitrateListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeUserLimitListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.api.listener.channel.user.PrivateChannelCreateListener;
import org.javacord.api.listener.channel.user.PrivateChannelDeleteListener;
import org.javacord.api.listener.connection.LostConnectionListener;
import org.javacord.api.listener.connection.ReconnectListener;
import org.javacord.api.listener.connection.ResumeListener;
import org.javacord.api.listener.message.CachedMessagePinListener;
import org.javacord.api.listener.message.CachedMessageUnpinListener;
import org.javacord.api.listener.message.ChannelPinsUpdateListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.MessageDeleteListener;
import org.javacord.api.listener.message.MessageEditListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveAllListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveListener;
import org.javacord.api.listener.server.ServerBecomesAvailableListener;
import org.javacord.api.listener.server.ServerBecomesUnavailableListener;
import org.javacord.api.listener.server.ServerChangeAfkChannelListener;
import org.javacord.api.listener.server.ServerChangeAfkTimeoutListener;
import org.javacord.api.listener.server.ServerChangeDefaultMessageNotificationLevelListener;
import org.javacord.api.listener.server.ServerChangeExplicitContentFilterLevelListener;
import org.javacord.api.listener.server.ServerChangeIconListener;
import org.javacord.api.listener.server.ServerChangeMultiFactorAuthenticationLevelListener;
import org.javacord.api.listener.server.ServerChangeNameListener;
import org.javacord.api.listener.server.ServerChangeOwnerListener;
import org.javacord.api.listener.server.ServerChangeRegionListener;
import org.javacord.api.listener.server.ServerChangeSplashListener;
import org.javacord.api.listener.server.ServerChangeSystemChannelListener;
import org.javacord.api.listener.server.ServerChangeVerificationLevelListener;
import org.javacord.api.listener.server.ServerJoinListener;
import org.javacord.api.listener.server.ServerLeaveListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiChangeNameListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiChangeWhitelistedRolesListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiCreateListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiDeleteListener;
import org.javacord.api.listener.server.member.ServerMemberBanListener;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;
import org.javacord.api.listener.server.member.ServerMemberUnbanListener;
import org.javacord.api.listener.server.role.RoleChangeColorListener;
import org.javacord.api.listener.server.role.RoleChangeHoistListener;
import org.javacord.api.listener.server.role.RoleChangeMentionableListener;
import org.javacord.api.listener.server.role.RoleChangeNameListener;
import org.javacord.api.listener.server.role.RoleChangePermissionsListener;
import org.javacord.api.listener.server.role.RoleChangePositionListener;
import org.javacord.api.listener.server.role.RoleCreateListener;
import org.javacord.api.listener.server.role.RoleDeleteListener;
import org.javacord.api.listener.server.role.UserRoleAddListener;
import org.javacord.api.listener.server.role.UserRoleRemoveListener;
import org.javacord.api.listener.user.UserChangeActivityListener;
import org.javacord.api.listener.user.UserChangeAvatarListener;
import org.javacord.api.listener.user.UserChangeDiscriminatorListener;
import org.javacord.api.listener.user.UserChangeNameListener;
import org.javacord.api.listener.user.UserChangeNicknameListener;
import org.javacord.api.listener.user.UserChangeStatusListener;
import org.javacord.api.listener.user.UserStartTypingListener;
import org.javacord.api.util.concurrent.ThreadPool;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.entity.activity.ActivityImpl;
import org.javacord.core.entity.activity.ApplicationInfoImpl;
import org.javacord.core.entity.emoji.CustomEmojiImpl;
import org.javacord.core.entity.emoji.KnownCustomEmojiImpl;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.entity.message.MessageSetImpl;
import org.javacord.core.entity.message.UncachedMessageUtilImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.server.invite.InviteImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.entity.webhook.WebhookImpl;
import org.javacord.core.util.ClassHelper;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.concurrent.ThreadPoolImpl;
import org.javacord.core.util.event.EventDispatcher;
import org.javacord.core.util.event.ListenerManagerImpl;
import org.javacord.core.util.gateway.DiscordWebSocketAdapter;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.ratelimit.RatelimitManager;
import org.javacord.core.util.rest.HttpClientWrapper;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import org.slf4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.http.HttpClient;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The implementation of {@link DiscordApi}.
 */
public class DiscordApiImpl implements DiscordApi {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(DiscordApiImpl.class);

    /**
     * The thread pool which is used internally.
     */
    private final ThreadPoolImpl threadPool = new ThreadPoolImpl();

    /**
     * The http client for this instance.
     */
    private final HttpClientWrapper httpClient;

    /**
     * The event dispatcher.
     */
    private final EventDispatcher eventDispatcher;

    /**
     * The object mapper for this instance.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * The ratelimit manager for this bot.
     */
    private final RatelimitManager ratelimitManager = new RatelimitManager(this);

    /**
     * The utility class to interact with uncached messages.
     */
    private final UncachedMessageUtil uncachedMessageUtil = new UncachedMessageUtilImpl(this);

    /**
     * The websocket adapter used to connect to Discord.
     */
    private volatile DiscordWebSocketAdapter websocketAdapter = null;

    /**
     * The account type of the bot.
     */
    private final AccountType accountType;

    /**
     * The token used for authentication.
     */
    private final String token;

    /**
     * Whether the {@link #disconnect()} method has been called before or not.
     */
    private volatile boolean disconnectCalled = false;

    /**
     * A lock to synchronize on {@link DiscordApiImpl#disconnectCalled}.
     */
    private final Object disconnectCalledLock = new Object();

    /**
     * The status which should be displayed for the bot.
     */
    private volatile UserStatus status = UserStatus.ONLINE;

    /**
     * The activity which should be displayed. May be <code>null</code>.
     */
    private volatile Activity activity;

    /**
     * The default message cache capacity which is applied for every newly created channel.
     */
    private volatile int defaultMessageCacheCapacity = 50;

    /**
     * The default maximum age of cached messages.
     */
    private volatile int defaultMessageCacheStorageTimeInSeconds = 60 * 60 * 12;

    /**
     * The function to calculate the reconnect delay.
     */
    private volatile Function<Integer, Integer> reconnectDelayProvider;

    /**
     * The current shard of the bot.
     */
    private final int currentShard;

    /**
     * The total amount of shards.
     */
    private final int totalShards;

    /**
     * Whether Javacord should wait for all servers to become available on startup or not.
     */
    private final boolean waitForServersOnStartup;

    /**
     * The user of the connected account.
     */
    private volatile User you;

    /**
     * The client id of the application.
     */
    private volatile long clientId = -1;

    /**
     * The id of the application's owner.
     */
    private volatile long ownerId = -1;

    /**
     * The time offset between the Discord time and our local time.
     */
    private volatile Long timeOffset = null;

    /**
     * A map which contains all users.
     */
    private final Map<Long, WeakReference<User>> users = Collections.synchronizedMap(new ConcurrentHashMap<>());

    /**
     * A map to retrieve user IDs by the weak ref that point to the respective
     * user or used to point to it for usage in the users cleanup,
     * as at cleanup time the weak ref is already emptied.
     */
    private final Map<Reference<? extends User>, Long> userIdByRef = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * The queue that is notified if a user became weakly-reachable.
     */
    private final ReferenceQueue<User> usersCleanupQueue = new ReferenceQueue<>();

    /**
     * A map which contains all servers that are ready.
     */
    private final ConcurrentHashMap<Long, Server> servers = new ConcurrentHashMap<>();

    /**
     * A map which contains all servers that are not ready.
     */
    private final ConcurrentHashMap<Long, Server> nonReadyServers = new ConcurrentHashMap<>();

    /**
     * A map which contains all group channels.
     */
    private final ConcurrentHashMap<Long, GroupChannel> groupChannels = new ConcurrentHashMap<>();

    /**
     * A set with all unavailable servers.
     */
    private final HashSet<Long> unavailableServers = new HashSet<>();

    /**
     * A map with all known custom emoji.
     */
    private final ConcurrentHashMap<Long, KnownCustomEmoji> customEmojis = new ConcurrentHashMap<>();

    /**
     * A map with all cached messages.
     */
    private final Map<Long, WeakReference<Message>> messages = Collections.synchronizedMap(new ConcurrentHashMap<>());

    /**
     * A map to retrieve message IDs by the weak ref that point to the respective
     * message or used to point to it for usage in the messages cleanup,
     * as at cleanup time the weak ref is already emptied.
     */
    private final Map<Reference<? extends Message>, Long> messageIdByRef
            = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * The queue that is notified if a message became weakly-reachable.
     */
    private final ReferenceQueue<Message> messagesCleanupQueue = new ReferenceQueue<>();

    /**
     * A map which contains all globally attachable listeners.
     * The key is the class of the listener.
     */
    private final Map<Class<? extends GloballyAttachableListener>,
            Map<GloballyAttachableListener, ListenerManagerImpl<? extends GloballyAttachableListener>>>
            listeners = Collections.synchronizedMap(new ConcurrentHashMap<>());

    /**
     * A map which contains all listeners which are assigned to a specific object instead of being global.
     * The key of the outer map is the class which the listener was registered to (e.g. Message.class).
     * The key of the first inner map is the id of the object.
     * The key of the second inner map is the class of the listener.
     * The key of the third inner map is the listener itself.
     * The final value is the listener manager.
     */
    private final Map<Class<?>, Map<Long, Map<Class<? extends ObjectAttachableListener>,
            Map<ObjectAttachableListener, ListenerManagerImpl<? extends ObjectAttachableListener>>>>>
            objectListeners = Collections.synchronizedMap(new ConcurrentHashMap<>());

    /**
     * Creates a new discord api instance that can be used for auto-ratelimited REST calls,
     * but does not connect to the Discord WebSocket.
     *
     * @param token The token used to connect without any account type specific prefix.
     */
    public DiscordApiImpl(String token) {
        this(AccountType.BOT, token, 0, 1, false, null);
    }

    /**
     * Creates a new discord api instance.
     *
     * @param accountType The account type of the instance.
     * @param token The token used to connect without any account type specific prefix.
     * @param currentShard The current shard the bot should connect to.
     * @param totalShards  The total amount of shards.
     * @param waitForServersOnStartup Whether Javacord should wait for all servers
     *                                to become available on startup or not.
     * @param ready The future which will be completed when the connection to Discord was successful.
     */
    public DiscordApiImpl(
            AccountType accountType,
            String token,
            int currentShard,
            int totalShards,
            boolean waitForServersOnStartup,
            CompletableFuture<DiscordApi> ready
    ) {
        this.accountType = accountType;
        this.token = accountType.getTokenPrefix() + token;
        this.currentShard = currentShard;
        this.totalShards = totalShards;
        this.waitForServersOnStartup = waitForServersOnStartup;
        this.reconnectDelayProvider = x ->
                (int) Math.round(Math.pow(x, 1.5) - (1 / (1 / (0.1 * x) + 1)) * Math.pow(x, 1.5)) + (currentShard * 6);

        this.httpClient = new HttpClientWrapper(HttpClient.newHttpClient());
        this.httpClient.addUniversalHeader("User-Agent", Javacord.USER_AGENT);
        this.httpClient.setLogger(LoggerUtil.getLogger(HttpClientWrapper.class)::trace);

        this.eventDispatcher = new EventDispatcher(this);

        if (ready != null) {
            getThreadPool().getExecutorService().submit(() -> {
                try {
                    this.websocketAdapter = new DiscordWebSocketAdapter(this);
                    this.websocketAdapter.isReady().whenComplete((readyReceived, throwable) -> {
                        if (readyReceived) {
                            if (accountType == AccountType.BOT) {
                                getApplicationInfo().whenComplete((applicationInfo, exception) -> {
                                    if (exception != null) {
                                        logger.error("Could not access self application info on startup!", exception);
                                    } else {
                                        clientId = applicationInfo.getClientId();
                                        ownerId = applicationInfo.getOwnerId();
                                    }
                                    ready.complete(this);
                                });
                            } else {
                                ready.complete(this);
                            }
                        } else {
                            threadPool.shutdown();
                            ready.completeExceptionally(
                                    new IllegalStateException("Websocket closed before READY packet was received!"));
                        }
                    });
                } catch (Throwable t) {
                    if (websocketAdapter != null) {
                        websocketAdapter.disconnect();
                    }
                    ready.completeExceptionally(t);
                }
            });

            // After minimum JDK 9 is required this can be switched to use a Cleaner
            getThreadPool().getScheduler().scheduleWithFixedDelay(() -> {
                try {
                    for (Reference<? extends User> userRef = usersCleanupQueue.poll();
                            userRef != null;
                            userRef = usersCleanupQueue.poll()) {
                        Long userId = userIdByRef.remove(userRef);
                        if (userId != null) {
                            users.remove(userId, userRef);
                        }
                    }
                } catch (Throwable t) {
                    logger.error("Failed to process users cleanup queue!", t);
                }
            }, 30, 30, TimeUnit.SECONDS);

            // After minimum JDK 9 is required this can be switched to use a Cleaner
            getThreadPool().getScheduler().scheduleWithFixedDelay(() -> {
                try {
                    for (Reference<? extends Message> messageRef = messagesCleanupQueue.poll();
                            messageRef != null;
                            messageRef = messagesCleanupQueue.poll()) {
                        Long messageId = messageIdByRef.remove(messageRef);
                        if (messageId != null) {
                            messages.remove(messageId, messageRef);
                        }
                    }
                } catch (Throwable t) {
                    logger.error("Failed to process messages cleanup queue!", t);
                }
            }, 30, 30, TimeUnit.SECONDS);

            // Add shutdown hook
            ready.thenAccept(api -> {
                WeakReference<DiscordApi> discordApiReference = new WeakReference<>(api);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> Optional.ofNullable(discordApiReference.get())
                        .ifPresent(DiscordApi::disconnect),
                        String.format("Javacord - Shutdown Disconnector (%s)", api)));
            });
        } else {
            WeakReference<DiscordApi> discordApiReference = new WeakReference<>(this);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> Optional.ofNullable(discordApiReference.get())
                    .ifPresent(DiscordApi::disconnect),
                    String.format("Javacord - Shutdown Disconnector (%s)", this)));
        }
    }

    /**
     * Gets the used {@link HttpClientWrapper http client} for this api instance.
     *
     * @return The used http client.
     */
    public HttpClientWrapper getHttpClient() {
        return httpClient;
    }

    /**
     * Gets the event dispatcher which is used to dispatch events.
     *
     * @return The used event dispatcher.
     */
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * Gets the ratelimit manager for this bot.
     *
     * @return The ratelimit manager for this bot.
     */
    public RatelimitManager getRatelimitManager() {
        return ratelimitManager;
    }

    /**
     * Gets the object mapper used by this api instance.
     *
     * @return The object mapper used by this api instance.
     */
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Purges all cached entities.
     * This method is only meant to be called after receiving a READY packet.
     */
    public void purgeCache() {
        synchronized (users) {
            users.values().stream()
                    .map(Reference::get)
                    .filter(Objects::nonNull)
                    .map(Cleanupable.class::cast)
                    .forEach(Cleanupable::cleanup);
            users.clear();
        }
        userIdByRef.clear();
        servers.values().stream()
                .map(Cleanupable.class::cast)
                .forEach(Cleanupable::cleanup);
        servers.clear();
        groupChannels.values().stream()
                .map(Cleanupable.class::cast)
                .forEach(Cleanupable::cleanup);
        groupChannels.clear();
        unavailableServers.clear();
        customEmojis.clear();
        messages.clear();
        messageIdByRef.clear();
        timeOffset = null;
    }

    /**
     * Gets a collection with all servers, including ready and not ready ones.
     *
     * @return A collection with all servers.
     */
    public Collection<Server> getAllServers() {
        return Collections.unmodifiableList(new ArrayList<>(nonReadyServers.values()));
    }

    /**
     * Gets a server by it's id, including ready and not ready ones.
     *
     * @param id The of the server.
     * @return The server with the given id.
     */
    public Optional<Server> getAllServerById(long id) {
        if (nonReadyServers.containsKey(id)) {
            return Optional.ofNullable(nonReadyServers.get(id));
        }
        return Optional.ofNullable(servers.get(id));
    }

    /**
     * Adds the given server to the cache.
     *
     * @param server The server to add.
     */
    public void addServerToCache(ServerImpl server) {
        // Remove in case, there's an old instance in cache
        removeServerFromCache(server.getId());

        nonReadyServers.put(server.getId(), server);
        server.addServerReadyConsumer(s -> {
            nonReadyServers.remove(s.getId());
            removeUnavailableServerFromCache(s.getId());
            servers.put(s.getId(), s);
        });
    }

    /**
     * Removes the given server from the cache.
     *
     * @param serverId The id of the server to remove.
     */
    public void removeServerFromCache(long serverId) {
        servers.computeIfPresent(serverId, (key, server) -> {
            ((Cleanupable) server).cleanup();
            return null;
        });
        nonReadyServers.computeIfPresent(serverId, (key, server) -> {
            ((Cleanupable) server).cleanup();
            return null;
        });
    }

    /**
     * Adds the given user to the cache.
     *
     * @param user The user to add.
     */
    public void addUserToCache(User user) {
        users.compute(user.getId(), (key, value) -> {
            Optional.ofNullable(value)
                    .map(Reference::get)
                    .filter(oldUser -> oldUser != user)
                    .map(Cleanupable.class::cast)
                    .ifPresent(Cleanupable::cleanup);

            WeakReference<User> result = new WeakReference<>(user, usersCleanupQueue);
            userIdByRef.put(result, key);
            return result;
        });
    }

    /**
     * Adds a group channel to the cache.
     *
     * @param channel The channel to add.
     */
    public void addGroupChannelToCache(GroupChannel channel) {
        GroupChannel oldChannel = groupChannels.put(channel.getId(), channel);
        if ((oldChannel != null) && (oldChannel != channel)) {
            ((Cleanupable) oldChannel).cleanup();
        }
    }

    /**
     * Removes a group channel from the cache.
     *
     * @param channelId The id of the channel to remove.
     */
    public void removeGroupChannelFromCache(long channelId) {
        groupChannels.computeIfPresent(channelId, (key, groupChannel) -> {
            ((Cleanupable) groupChannel).cleanup();
            return null;
        });
    }

    /**
     * Adds a server id to the list with unavailable servers.
     *
     * @param serverId The id of the server.
     */
    public void addUnavailableServerToCache(long serverId) {
        unavailableServers.add(serverId);
    }

    /**
     * Removes a server id from the list with unavailable servers.
     *
     * @param serverId The id of the server.
     */
    private void removeUnavailableServerFromCache(long serverId) {
        unavailableServers.remove(serverId);
    }

    /**
     * Sets the user of the connected account.
     *
     * @param yourself The user of the connected account.
     */
    public void setYourself(User yourself) {
        you = yourself;
    }

    /**
     * Gets the time offset between the Discord time and our local time.
     * Might be <code>null</code> if it hasn't been calculated yet.
     *
     * @return The time offset between the Discord time and our local time.
     */
    public Long getTimeOffset() {
        return timeOffset;
    }

    /**
     * Sets the time offset between the Discord time and our local time.
     *
     * @param timeOffset The time offset to set.
     */
    public void setTimeOffset(Long timeOffset) {
        this.timeOffset = timeOffset;
    }

    /**
     * Gets a user or creates a new one from the given data.
     *
     * @param data The json data of the user.
     * @return The user.
     */
    public User getOrCreateUser(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        synchronized (users) {
            return getCachedUserById(id).orElseGet(() -> {
                if (!data.has("username")) {
                    throw new IllegalStateException("Couldn't get or created user. Please inform the developer!");
                }
                return new UserImpl(this, data);
            });
        }
    }

    /**
     * Gets or creates a new known custom emoji object.
     *
     * @param server The server of the emoji.
     * @param data The data of the emoji.
     * @return The emoji for the given json object.
     */
    public KnownCustomEmoji getOrCreateKnownCustomEmoji(Server server, JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        return customEmojis.computeIfAbsent(id, key -> new KnownCustomEmojiImpl(this, server, data));
    }

    /**
     * Gets a known custom emoji or creates a new (unknown) custom emoji object.
     *
     * @param data The data of the emoji.
     * @return The emoji for the given json object.
     */
    public CustomEmoji getKnownCustomEmojiOrCreateCustomEmoji(JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        CustomEmoji emoji = customEmojis.get(id);
        return emoji == null ? new CustomEmojiImpl(this, data) : emoji;
    }

    /**
     * Gets a known custom emoji or creates a new (unknown) custom emoji object.
     *
     * @param id The id of the emoji.
     * @param name The name of the emoji.
     * @param animated Whether the emoji is animated or not.
     * @return The emoji for the given json object.
     */
    public CustomEmoji getKnownCustomEmojiOrCreateCustomEmoji(long id, String name, boolean animated) {
        CustomEmoji emoji = customEmojis.get(id);
        return emoji == null ? new CustomEmojiImpl(this, id, name, animated) : emoji;
    }

    /**
     * Removes a known custom emoji object.
     *
     * @param emoji The emoji to remove.
     */
    public void removeCustomEmoji(KnownCustomEmoji emoji) {
        customEmojis.remove(emoji.getId());
    }

    /**
     * Gets or creates a new message object.
     *
     * @param channel The channel of the message.
     * @param data The data of the message.
     * @return The message for the given json object.
     */
    public Message getOrCreateMessage(TextChannel channel, JsonNode data) {
        long id = Long.parseLong(data.get("id").asText());
        synchronized (messages) {
            return getCachedMessageById(id).orElseGet(() -> new MessageImpl(this, channel, data));
        }
    }

    /**
     * Adds a message to the cache.
     *
     * @param message The message to add.
     */
    public void addMessageToCache(Message message) {
        messages.compute(message.getId(), (key, value) -> {
            if ((value == null) || (value.get() == null)) {
                WeakReference<Message> result = new WeakReference<>(message, messagesCleanupQueue);
                messageIdByRef.put(result, key);
                return result;
            }
            return value;
        });
    }

    /**
     * Removes a message from the cache.
     *
     * @param messageId The id of the message to remove.
     */
    public void removeMessageFromCache(long messageId) {
        WeakReference<Message> messageRef = messages.remove(messageId);
        if (messageRef != null) {
            messageIdByRef.remove(messageRef, messageId);
        }
    }

    /**
     * Adds an object listener.
     * Adding a listener multiple times to the same object will only add it once
     * and return the same listener manager on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param listenerClass The listener class.
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The manager for the added listener.
     */
    @SuppressWarnings("unchecked")
    public <T extends ObjectAttachableListener> ListenerManager<T> addObjectListener(
            Class<?> objectClass, long objectId, Class<T> listenerClass, T listener) {
        Map<ObjectAttachableListener, ListenerManagerImpl<? extends ObjectAttachableListener>> listeners =
                objectListeners
                        .computeIfAbsent(objectClass, key -> new ConcurrentHashMap<>())
                        .computeIfAbsent(objectId, key -> new ConcurrentHashMap<>())
                        .computeIfAbsent(listenerClass, c -> Collections.synchronizedMap(new LinkedHashMap<>()));
        return (ListenerManager<T>) listeners.computeIfAbsent(
                listener, key -> new ListenerManagerImpl<>(this, listener, listenerClass, objectClass, objectId));
    }

    /**
     * Removes an object listener.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    public <T extends ObjectAttachableListener> void removeObjectListener(
            Class<?> objectClass, long objectId, Class<T> listenerClass, T listener) {
        synchronized (objectListeners) {
            if (objectClass == null) {
                return;
            }
            Map<Long, Map<Class<? extends ObjectAttachableListener>, Map<ObjectAttachableListener,
                    ListenerManagerImpl<? extends ObjectAttachableListener>>>> objectListener =
                    objectListeners.get(objectClass);
            if (objectListener == null) {
                return;
            }
            Map<Class<? extends ObjectAttachableListener>, Map<ObjectAttachableListener,
                    ListenerManagerImpl<? extends ObjectAttachableListener>>> listeners = objectListener.get(objectId);
            if (listeners == null) {
                return;
            }
            Map<ObjectAttachableListener, ListenerManagerImpl<? extends ObjectAttachableListener>> classListeners =
                    listeners.get(listenerClass);
            if (classListeners == null) {
                return;
            }
            ListenerManagerImpl<? extends ObjectAttachableListener> listenerManager = classListeners.get(listener);
            if (listenerManager == null) {
                return;
            }
            classListeners.remove(listener);
            listenerManager.removed();
            // Clean it up
            if (classListeners.isEmpty()) {
                listeners.remove(listenerClass);
                if (listeners.isEmpty()) {
                    objectListener.remove(objectId);
                    if (objectListener.isEmpty()) {
                        objectListeners.remove(objectClass);
                    }
                }
            }
        }
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code ObjectAttachableListener}s and their
     * assigned listener classes they listen to.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code ObjectAttachableListener}s and
     *     their assigned listener classes they listen to.
     */
    @SuppressWarnings("unchecked")
    public <T extends ObjectAttachableListener> Map<T, List<Class<T>>> getObjectListeners(
            Class<?> objectClass, long objectId) {
        return Collections.unmodifiableMap(Optional.ofNullable(objectClass)
                .map(objectListeners::get)
                .map(objectListener -> objectListener.get(objectId))
                .map(Map::entrySet)
                .map(Set::stream)
                .map(entryStream -> entryStream
                        .flatMap(entry -> entry
                                .getValue()
                                .keySet()
                                .stream()
                                .map(listener -> new SimpleEntry<>((T) listener, (Class<T>) entry.getKey()))))
                .map(entryStream -> entryStream
                        .collect(Collectors.groupingBy(Entry::getKey,
                                                       Collectors.mapping(Entry::getValue, Collectors.toList()))))
                .orElseGet(HashMap::new));
    }

    /**
     * Gets all object listeners of the given class.
     *
     * @param objectClass The class of the object.
     * @param objectId The id of the object.
     * @param listenerClass The listener class.
     * @param <T> The type of the listener.
     * @return A list with all object listeners of the given type.
     */
    @SuppressWarnings("unchecked")
    public <T extends ObjectAttachableListener> List<T> getObjectListeners(
            Class<?> objectClass, long objectId, Class<T> listenerClass) {
        return Collections.unmodifiableList((List<T>) Optional.ofNullable(objectClass)
                .map(objectListeners::get)
                .map(objectListener -> objectListener.get(objectId))
                .map(listeners -> listeners.get(listenerClass))
                .map(Map::keySet)
                .map(ArrayList::new)
                .orElseGet(ArrayList::new));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GloballyAttachableListener> Map<T, List<Class<T>>> getListeners() {
        return Collections.unmodifiableMap(listeners.entrySet().stream()
                .flatMap(entry -> entry
                        .getValue()
                        .keySet()
                        .stream()
                        .map(listener -> new SimpleEntry<>((T) listener, (Class<T>) entry.getKey())))
                .collect(Collectors.groupingBy(Entry::getKey,
                                               Collectors.mapping(Entry::getValue, Collectors.toList()))));
    }

    /**
     * Gets all globally attachable listeners of the given class.
     *
     * @param listenerClass The class of the listener.
     * @param <T> The class of the listener.
     * @return A list with all listeners of the given type.
     */
    @SuppressWarnings("unchecked")
    public <T extends GloballyAttachableListener> List<T> getListeners(Class<T> listenerClass) {
        return Collections.unmodifiableList((List<T>) Optional.ofNullable(listenerClass)
                .map(listeners::get)
                .map(Map::keySet)
                .map(ArrayList::new)
                .orElseGet(ArrayList::new));
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public ThreadPool getThreadPool() {
        return threadPool;
    }

    @Override
    public UncachedMessageUtil getUncachedMessageUtil() {
        return uncachedMessageUtil;
    }

    /*
     * Note: You might think the return type should be Optional<WebsocketAdapter>, because it's null till we receive
     *       the gateway from Discord. However the DiscordApi instance is only passed to the user, AFTER we connect
     *       so for the end user it is in fact never null.
     */
    /**
     * Gets the websocket adapter which is used to connect to Discord.
     *
     * @return The websocket adapter.
     */
    public DiscordWebSocketAdapter getWebSocketAdapter() {
        return websocketAdapter;
    }

    @Override
    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public void setMessageCacheSize(int capacity, int storageTimeInSeconds) {
        this.defaultMessageCacheCapacity = capacity;
        this.defaultMessageCacheStorageTimeInSeconds = storageTimeInSeconds;
        getChannels().stream()
                .filter(channel -> channel instanceof TextChannel)
                .map(channel -> (TextChannel) channel)
                .forEach(channel -> {
                    channel.getMessageCache().setCapacity(capacity);
                    channel.getMessageCache().setStorageTimeInSeconds(storageTimeInSeconds);
                });
    }

    @Override
    public int getDefaultMessageCacheCapacity() {
        return defaultMessageCacheCapacity;
    }

    @Override
    public int getDefaultMessageCacheStorageTimeInSeconds() {
        return defaultMessageCacheStorageTimeInSeconds;
    }

    @Override
    public int getCurrentShard() {
        return currentShard;
    }

    @Override
    public int getTotalShards() {
        return totalShards;
    }

    @Override
    public boolean isWaitingForServersOnStartup() {
        return waitForServersOnStartup;
    }

    @Override
    public void updateStatus(UserStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("The status cannot be null");
        }
        this.status = status;
        websocketAdapter.updateStatus();
    }

    @Override
    public UserStatus getStatus() {
        return status;
    }


    /**
     * Sets the current activity, along with type and streaming Url.
     *
     * @param name The name of the activity.
     * @param type The activity's type.
     * @param streamingUrl The Url used for streaming.
     */
    private void updateActivity(String name, ActivityType type, String streamingUrl) {
        if (name == null) {
            activity = null;
        } else if (streamingUrl == null) {
            activity = new ActivityImpl(type, name, null);
        } else {
            activity = new ActivityImpl(type, name, streamingUrl);
        }
        websocketAdapter.updateStatus();
    }


    @Override
    public void updateActivity(String name) {
        updateActivity(name, ActivityType.PLAYING, null);
    }

    @Override
    public void updateActivity(String name, ActivityType type) {
        updateActivity(name, type, null);
    }

    @Override
    public void updateActivity(String name, String streamingUrl) {
        updateActivity(name, ActivityType.STREAMING, streamingUrl);
    }

    @Override
    public void unsetActivity() {
        updateActivity(null);
    }

    @Override
    public Optional<Activity> getActivity() {
        return Optional.ofNullable(activity);
    }

    @Override
    public User getYourself() {
        return you;
    }

    @Override
    public long getOwnerId() {
        if (accountType != AccountType.BOT) {
            throw new IllegalStateException("Cannot get owner id of non bot accounts");
        }
        return ownerId;
    }

    @Override
    public long getClientId() {
        if (accountType != AccountType.BOT) {
            throw new IllegalStateException("Cannot get client id of non bot accounts");
        }
        return clientId;
    }

    @Override
    public void disconnect() {
        synchronized (disconnectCalledLock) {
            if (!disconnectCalled) {
                if (websocketAdapter == null) {
                    // if no web socket is connected, immediately shutdown thread pool
                    threadPool.shutdown();
                } else {
                    // shutdown thread pool after web socket disconnected event was dispatched
                    addLostConnectionListener(event -> threadPool.shutdown());
                    // disconnect web socket
                    websocketAdapter.disconnect();
                    // shutdown thread pool if within one minute no disconnect event was dispatched
                    threadPool.getDaemonScheduler().schedule(threadPool::shutdown, 1, TimeUnit.MINUTES);
                }
                //TODO: Find replacement for these under HttpClient
                //httpClient.dispatcher().executorService().shutdown();
                //httpClient.connectionPool().evictAll();
                ratelimitManager.cleanup();
            }
            disconnectCalled = true;
        }
    }

    @Override
    public void setReconnectDelay(Function<Integer, Integer> reconnectDelayProvider) {
        this.reconnectDelayProvider = reconnectDelayProvider;
    }

    @Override
    public int getReconnectDelay(int attempt) {
        if (attempt < 0) {
            throw new IllegalArgumentException("attempt must be 1 or greater");
        }
        return reconnectDelayProvider.apply(attempt);
    }

    @Override
    public CompletableFuture<ApplicationInfo> getApplicationInfo() {
        return new RestRequest<ApplicationInfo>(this, RestMethod.GET, RestEndpoint.SELF_INFO)
                .execute(result -> new ApplicationInfoImpl(this, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<Webhook> getWebhookById(long id) {
        return new RestRequest<Webhook>(this, RestMethod.GET, RestEndpoint.WEBHOOK)
                .setUrlParameters(Long.toUnsignedString(id))
                .execute(result -> new WebhookImpl(this, result.getJsonBody()));
    }

    @Override
    public Collection<Long> getUnavailableServers() {
        return Collections.unmodifiableCollection(unavailableServers);
    }

    @Override
    public CompletableFuture<Invite> getInviteByCode(String code) {
        return new RestRequest<Invite>(this, RestMethod.GET, RestEndpoint.INVITE)
                .addQueryParameter("with_counts", "false")
                .execute(result -> new InviteImpl(this, result.getJsonBody()));
    }

    @Override
    public CompletableFuture<Invite> getInviteWithMemberCountsByCode(String code) {
        return new RestRequest<Invite>(this, RestMethod.GET, RestEndpoint.INVITE)
                .setUrlParameters(code)
                .addQueryParameter("with_counts", "true")
                .execute(result -> new InviteImpl(this, result.getJsonBody()));
    }

    @Override
    public Collection<User> getCachedUsers() {
        synchronized (users) {
            return Collections.unmodifiableCollection(users.values().stream()
                    .map(Reference::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public Optional<User> getCachedUserById(long id) {
        return Optional.ofNullable(users.get(id)).map(Reference::get);
    }

    @Override
    public CompletableFuture<User> getUserById(long id) {
        return getCachedUserById(id)
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> new RestRequest<User>(this, RestMethod.GET, RestEndpoint.USER)
                .setUrlParameters(Long.toUnsignedString(id))
                .execute(result -> this.getOrCreateUser(result.getJsonBody())));
    }

    @Override
    public MessageSet getCachedMessages() {
        synchronized (messages) {
            return messages.values().stream()
                    .map(Reference::get)
                    .filter(Objects::nonNull).collect(Collectors.toCollection(MessageSetImpl::new));
        }
    }

    @Override
    public Optional<Message> getCachedMessageById(long id) {
        return Optional.ofNullable(messages.get(id)).map(Reference::get);
    }

    @Override
    public Collection<Server> getServers() {
        return Collections.unmodifiableList(new ArrayList<>(servers.values()));
    }

    @Override
    public Optional<Server> getServerById(long id) {
        return Optional.ofNullable(servers.get(id));
    }

    @Override
    public Collection<KnownCustomEmoji> getCustomEmojis() {
        return Collections.unmodifiableCollection(customEmojis.values());
    }

    @Override
    public Optional<KnownCustomEmoji> getCustomEmojiById(long id) {
        return Optional.ofNullable(customEmojis.get(id));
    }

    @Override
    public Collection<GroupChannel> getGroupChannels() {
        return Collections.unmodifiableCollection(groupChannels.values());
    }

    @Override
    public Optional<GroupChannel> getGroupChannelById(long id) {
        return Optional.ofNullable(groupChannels.get(id));
    }

    @Override
    public ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener) {
        return addListener(MessageCreateListener.class, listener);
    }

    @Override
    public List<MessageCreateListener> getMessageCreateListeners() {
        return getListeners(MessageCreateListener.class);
    }

    @Override
    public ListenerManager<ServerJoinListener> addServerJoinListener(ServerJoinListener listener) {
        return addListener(ServerJoinListener.class, listener);
    }

    @Override
    public List<ServerJoinListener> getServerJoinListeners() {
        return getListeners(ServerJoinListener.class);
    }

    @Override
    public ListenerManager<ServerLeaveListener> addServerLeaveListener(ServerLeaveListener listener) {
        return addListener(ServerLeaveListener.class, listener);
    }

    @Override
    public List<ServerLeaveListener> getServerLeaveListeners() {
        return getListeners(ServerLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerBecomesAvailableListener> addServerBecomesAvailableListener(
            ServerBecomesAvailableListener listener) {
        return addListener(ServerBecomesAvailableListener.class, listener);
    }

    @Override
    public List<ServerBecomesAvailableListener> getServerBecomesAvailableListeners() {
        return getListeners(ServerBecomesAvailableListener.class);
    }

    @Override
    public ListenerManager<ServerBecomesUnavailableListener> addServerBecomesUnavailableListener(
            ServerBecomesUnavailableListener listener) {
        return addListener(ServerBecomesUnavailableListener.class, listener);
    }

    @Override
    public List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners() {
        return getListeners(ServerBecomesUnavailableListener.class);
    }

    @Override
    public ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener) {
        return addListener(UserStartTypingListener.class, listener);
    }

    @Override
    public List<UserStartTypingListener> getUserStartTypingListeners() {
        return getListeners(UserStartTypingListener.class);
    }

    @Override
    public ListenerManager<ServerChannelCreateListener> addServerChannelCreateListener(
            ServerChannelCreateListener listener) {
        return addListener(ServerChannelCreateListener.class, listener);
    }

    @Override
    public List<ServerChannelCreateListener> getServerChannelCreateListeners() {
        return getListeners(ServerChannelCreateListener.class);
    }

    @Override
    public ListenerManager<ServerChannelDeleteListener> addServerChannelDeleteListener(
            ServerChannelDeleteListener listener) {
        return addListener(ServerChannelDeleteListener.class, listener);
    }

    @Override
    public List<ServerChannelDeleteListener> getServerChannelDeleteListeners() {
        return getListeners(ServerChannelDeleteListener.class);
    }

    @Override
    public ListenerManager<PrivateChannelCreateListener> addPrivateChannelCreateListener(
            PrivateChannelCreateListener listener) {
        return addListener(PrivateChannelCreateListener.class, listener);
    }

    @Override
    public List<PrivateChannelCreateListener> getPrivateChannelCreateListeners() {
        return getListeners(PrivateChannelCreateListener.class);
    }

    @Override
    public ListenerManager<PrivateChannelDeleteListener> addPrivateChannelDeleteListener(
            PrivateChannelDeleteListener listener) {
        return addListener(PrivateChannelDeleteListener.class, listener);
    }

    @Override
    public List<PrivateChannelDeleteListener> getPrivateChannelDeleteListeners() {
        return getListeners(PrivateChannelDeleteListener.class);
    }

    @Override
    public ListenerManager<GroupChannelCreateListener> addGroupChannelCreateListener(
            GroupChannelCreateListener listener) {
        return addListener(GroupChannelCreateListener.class, listener);
    }

    @Override
    public List<GroupChannelCreateListener> getGroupChannelCreateListeners() {
        return getListeners(GroupChannelCreateListener.class);
    }

    @Override
    public ListenerManager<GroupChannelChangeNameListener> addGroupChannelChangeNameListener(
            GroupChannelChangeNameListener listener) {
        return addListener(GroupChannelChangeNameListener.class, listener);
    }

    @Override
    public ListenerManager<GroupChannelDeleteListener> addGroupChannelDeleteListener(
            GroupChannelDeleteListener listener) {
        return addListener(GroupChannelDeleteListener.class, listener);
    }

    @Override
    public List<GroupChannelDeleteListener> getGroupChannelDeleteListeners() {
        return getListeners(GroupChannelDeleteListener.class);
    }

    @Override
    public List<GroupChannelChangeNameListener> getGroupChannelChangeNameListeners() {
        return getListeners(GroupChannelChangeNameListener.class);
    }

    @Override
    public ListenerManager<MessageDeleteListener> addMessageDeleteListener(MessageDeleteListener listener) {
        return addListener(MessageDeleteListener.class, listener);
    }

    @Override
    public List<MessageDeleteListener> getMessageDeleteListeners() {
        return getListeners(MessageDeleteListener.class);
    }

    @Override
    public ListenerManager<MessageEditListener> addMessageEditListener(MessageEditListener listener) {
        return addListener(MessageEditListener.class, listener);
    }

    @Override
    public List<MessageEditListener> getMessageEditListeners() {
        return getListeners(MessageEditListener.class);
    }

    @Override
    public ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener) {
        return addListener(ReactionAddListener.class, listener);
    }

    @Override
    public List<ReactionAddListener> getReactionAddListeners() {
        return getListeners(ReactionAddListener.class);
    }

    @Override
    public ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener) {
        return addListener(ReactionRemoveListener.class, listener);
    }

    @Override
    public List<ReactionRemoveListener> getReactionRemoveListeners() {
        return getListeners(ReactionRemoveListener.class);
    }

    @Override
    public ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(ReactionRemoveAllListener listener) {
        return addListener(ReactionRemoveAllListener.class, listener);
    }

    @Override
    public List<ReactionRemoveAllListener> getReactionRemoveAllListeners() {
        return getListeners(ReactionRemoveAllListener.class);
    }

    @Override
    public ListenerManager<ServerMemberJoinListener> addServerMemberJoinListener(ServerMemberJoinListener listener) {
        return addListener(ServerMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerMemberJoinListener> getServerMemberJoinListeners() {
        return getListeners(ServerMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerMemberLeaveListener> addServerMemberLeaveListener(
            ServerMemberLeaveListener listener) {
        return addListener(ServerMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerMemberLeaveListener> getServerMemberLeaveListeners() {
        return getListeners(ServerMemberLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerMemberBanListener> addServerMemberBanListener(ServerMemberBanListener listener) {
        return addListener(ServerMemberBanListener.class, listener);
    }

    @Override
    public List<ServerMemberBanListener> getServerMemberBanListeners() {
        return getListeners(ServerMemberBanListener.class);
    }

    @Override
    public ListenerManager<ServerMemberUnbanListener> addServerMemberUnbanListener(ServerMemberUnbanListener listener) {
        return addListener(ServerMemberUnbanListener.class, listener);
    }

    @Override
    public List<ServerMemberUnbanListener> getServerMemberUnbanListeners() {
        return getListeners(ServerMemberUnbanListener.class);
    }

    @Override
    public ListenerManager<ServerChangeNameListener> addServerChangeNameListener(ServerChangeNameListener listener) {
        return addListener(ServerChangeNameListener.class, listener);
    }

    @Override
    public List<ServerChangeNameListener> getServerChangeNameListeners() {
        return getListeners(ServerChangeNameListener.class);
    }

    @Override
    public ListenerManager<ServerChangeIconListener> addServerChangeIconListener(ServerChangeIconListener listener) {
        return addListener(ServerChangeIconListener.class, listener);
    }

    @Override
    public List<ServerChangeIconListener> getServerChangeIconListeners() {
        return getListeners(ServerChangeIconListener.class);
    }

    @Override
    public ListenerManager<ServerChangeSplashListener> addServerChangeSplashListener(
            ServerChangeSplashListener listener) {
        return addListener(ServerChangeSplashListener.class, listener);
    }

    @Override
    public List<ServerChangeSplashListener> getServerChangeSplashListeners() {
        return getListeners(ServerChangeSplashListener.class);
    }

    @Override
    public ListenerManager<ServerChangeVerificationLevelListener> addServerChangeVerificationLevelListener(
            ServerChangeVerificationLevelListener listener) {
        return addListener(ServerChangeVerificationLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeVerificationLevelListener> getServerChangeVerificationLevelListeners() {
        return getListeners(ServerChangeVerificationLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeRegionListener> addServerChangeRegionListener(
            ServerChangeRegionListener listener) {
        return addListener(ServerChangeRegionListener.class, listener);
    }

    @Override
    public List<ServerChangeRegionListener> getServerChangeRegionListeners() {
        return getListeners(ServerChangeRegionListener.class);
    }

    @Override
    public ListenerManager<ServerChangeDefaultMessageNotificationLevelListener>
            addServerChangeDefaultMessageNotificationLevelListener(
                    ServerChangeDefaultMessageNotificationLevelListener listener) {
        return addListener(ServerChangeDefaultMessageNotificationLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeDefaultMessageNotificationLevelListener>
            getServerChangeDefaultMessageNotificationLevelListeners() {
        return getListeners(ServerChangeDefaultMessageNotificationLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeMultiFactorAuthenticationLevelListener>
            addServerChangeMultiFactorAuthenticationLevelListener(
                    ServerChangeMultiFactorAuthenticationLevelListener listener) {
        return addListener(ServerChangeMultiFactorAuthenticationLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeMultiFactorAuthenticationLevelListener>
            getServerChangeMultiFactorAuthenticationLevelListeners() {
        return getListeners(ServerChangeMultiFactorAuthenticationLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeOwnerListener> addServerChangeOwnerListener(ServerChangeOwnerListener listener) {
        return addListener(ServerChangeOwnerListener.class, listener);
    }

    @Override
    public List<ServerChangeOwnerListener> getServerChangeOwnerListeners() {
        return getListeners(ServerChangeOwnerListener.class);
    }

    @Override
    public ListenerManager<ServerChangeExplicitContentFilterLevelListener>
            addServerChangeExplicitContentFilterLevelListener(
                    ServerChangeExplicitContentFilterLevelListener listener) {
        return addListener(ServerChangeExplicitContentFilterLevelListener.class, listener);
    }

    @Override
    public List<ServerChangeExplicitContentFilterLevelListener> getServerChangeExplicitContentFilterLevelListeners() {
        return getListeners(ServerChangeExplicitContentFilterLevelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeSystemChannelListener> addServerChangeSystemChannelListener(
            ServerChangeSystemChannelListener listener) {
        return addListener(ServerChangeSystemChannelListener.class, listener);
    }

    @Override
    public List<ServerChangeSystemChannelListener> getServerChangeSystemChannelListeners() {
        return getListeners(ServerChangeSystemChannelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeAfkChannelListener> addServerChangeAfkChannelListener(
            ServerChangeAfkChannelListener listener) {
        return addListener(ServerChangeAfkChannelListener.class, listener);
    }

    @Override
    public List<ServerChangeAfkChannelListener> getServerChangeAfkChannelListeners() {
        return getListeners(ServerChangeAfkChannelListener.class);
    }

    @Override
    public ListenerManager<ServerChangeAfkTimeoutListener> addServerChangeAfkTimeoutListener(
            ServerChangeAfkTimeoutListener listener) {
        return addListener(ServerChangeAfkTimeoutListener.class, listener);
    }

    @Override
    public List<ServerChangeAfkTimeoutListener> getServerChangeAfkTimeoutListeners() {
        return getListeners(ServerChangeAfkTimeoutListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeNameListener> addServerChannelChangeNameListener(
            ServerChannelChangeNameListener listener) {
        return addListener(ServerChannelChangeNameListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners() {
        return getListeners(ServerChannelChangeNameListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangePositionListener> addServerChannelChangePositionListener(
            ServerChannelChangePositionListener listener) {
        return addListener(ServerChannelChangePositionListener.class, listener);
    }

    @Override
    public List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners() {
        return getListeners(ServerChannelChangePositionListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeNsfwFlagListener> addServerChannelChangeNsfwFlagListener(
            ServerChannelChangeNsfwFlagListener listener) {
        return addListener(ServerChannelChangeNsfwFlagListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeNsfwFlagListener> getServerChannelChangeNsfwFlagListeners() {
        return getListeners(ServerChannelChangeNsfwFlagListener.class);
    }

    @Override
    public ListenerManager<KnownCustomEmojiCreateListener> addKnownCustomEmojiCreateListener(
            KnownCustomEmojiCreateListener listener) {
        return addListener(KnownCustomEmojiCreateListener.class, listener);
    }

    @Override
    public List<KnownCustomEmojiCreateListener> getKnownCustomEmojiCreateListeners() {
        return getListeners(KnownCustomEmojiCreateListener.class);
    }

    @Override
    public ListenerManager<KnownCustomEmojiChangeNameListener> addKnownCustomEmojiChangeNameListener(
            KnownCustomEmojiChangeNameListener listener) {
        return addListener(KnownCustomEmojiChangeNameListener.class, listener);
    }

    @Override
    public List<KnownCustomEmojiChangeNameListener> getKnownCustomEmojiChangeNameListeners() {
        return getListeners(KnownCustomEmojiChangeNameListener.class);
    }

    @Override
    public ListenerManager<KnownCustomEmojiChangeWhitelistedRolesListener>
            addKnownCustomEmojiChangeWhitelistedRolesListener(KnownCustomEmojiChangeWhitelistedRolesListener listener) {
        return addListener(KnownCustomEmojiChangeWhitelistedRolesListener.class, listener);
    }

    @Override
    public List<KnownCustomEmojiChangeWhitelistedRolesListener> getKnownCustomEmojiChangeWhitelistedRolesListeners() {
        return getListeners(KnownCustomEmojiChangeWhitelistedRolesListener.class);
    }

    @Override
    public ListenerManager<KnownCustomEmojiDeleteListener> addKnownCustomEmojiDeleteListener(
            KnownCustomEmojiDeleteListener listener) {
        return addListener(KnownCustomEmojiDeleteListener.class, listener);
    }

    @Override
    public List<KnownCustomEmojiDeleteListener> getKnownCustomEmojiDeleteListeners() {
        return getListeners(KnownCustomEmojiDeleteListener.class);
    }

    @Override
    public ListenerManager<UserChangeActivityListener> addUserChangeActivityListener(
            UserChangeActivityListener listener) {
        return addListener(UserChangeActivityListener.class, listener);
    }

    @Override
    public List<UserChangeActivityListener> getUserChangeActivityListeners() {
        return getListeners(UserChangeActivityListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener) {
        return addListener(ServerVoiceChannelMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners() {
        return getListeners(ServerVoiceChannelMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener) {
        return addListener(ServerVoiceChannelMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners() {
        return getListeners(ServerVoiceChannelMemberLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeBitrateListener> addServerVoiceChannelChangeBitrateListener(
            ServerVoiceChannelChangeBitrateListener listener) {
        return addListener(ServerVoiceChannelChangeBitrateListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelChangeBitrateListener> getServerVoiceChannelChangeBitrateListeners() {
        return getListeners(ServerVoiceChannelChangeBitrateListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeUserLimitListener> addServerVoiceChannelChangeUserLimitListener(
            ServerVoiceChannelChangeUserLimitListener listener) {
        return addListener(ServerVoiceChannelChangeUserLimitListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelChangeUserLimitListener> getServerVoiceChannelChangeUserLimitListeners() {
        return getListeners(ServerVoiceChannelChangeUserLimitListener.class);
    }

    @Override
    public ListenerManager<UserChangeStatusListener> addUserChangeStatusListener(UserChangeStatusListener listener) {
        return addListener(UserChangeStatusListener.class, listener);
    }

    @Override
    public List<UserChangeStatusListener> getUserChangeStatusListeners() {
        return getListeners(UserChangeStatusListener.class);
    }

    @Override
    public ListenerManager<RoleChangeColorListener> addRoleChangeColorListener(RoleChangeColorListener listener) {
        return addListener(RoleChangeColorListener.class, listener);
    }

    @Override
    public List<RoleChangeColorListener> getRoleChangeColorListeners() {
        return getListeners(RoleChangeColorListener.class);
    }

    @Override
    public ListenerManager<RoleChangeHoistListener> addRoleChangeHoistListener(RoleChangeHoistListener listener) {
        return addListener(RoleChangeHoistListener.class, listener);
    }

    @Override
    public List<RoleChangeHoistListener> getRoleChangeHoistListeners() {
        return getListeners(RoleChangeHoistListener.class);
    }

    @Override
    public ListenerManager<RoleChangeMentionableListener> addRoleChangeMentionableListener(
            RoleChangeMentionableListener listener) {
        return addListener(RoleChangeMentionableListener.class, listener);
    }

    @Override
    public List<RoleChangeMentionableListener> getRoleChangeMentionableListeners() {
        return getListeners(RoleChangeMentionableListener.class);
    }

    @Override
    public ListenerManager<RoleChangeNameListener> addRoleChangeNameListener(RoleChangeNameListener listener) {
        return addListener(RoleChangeNameListener.class, listener);
    }

    @Override
    public List<RoleChangeNameListener> getRoleChangeNameListeners() {
        return getListeners(RoleChangeNameListener.class);
    }

    @Override
    public ListenerManager<RoleChangePermissionsListener> addRoleChangePermissionsListener(
            RoleChangePermissionsListener listener) {
        return addListener(RoleChangePermissionsListener.class, listener);
    }

    @Override
    public List<RoleChangePermissionsListener> getRoleChangePermissionsListeners() {
        return getListeners(RoleChangePermissionsListener.class);
    }

    @Override
    public ListenerManager<RoleChangePositionListener> addRoleChangePositionListener(
            RoleChangePositionListener listener) {
        return addListener(RoleChangePositionListener.class, listener);
    }

    @Override
    public List<RoleChangePositionListener> getRoleChangePositionListeners() {
        return getListeners(RoleChangePositionListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
            addServerChannelChangeOverwrittenPermissionsListener(
                    ServerChannelChangeOverwrittenPermissionsListener listener) {
        return addListener(ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeOverwrittenPermissionsListener>
            getServerChannelChangeOverwrittenPermissionsListeners() {
        return getListeners(ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    @Override
    public ListenerManager<RoleCreateListener> addRoleCreateListener(RoleCreateListener listener) {
        return addListener(RoleCreateListener.class, listener);
    }

    @Override
    public List<RoleCreateListener> getRoleCreateListeners() {
        return getListeners(RoleCreateListener.class);
    }

    @Override
    public ListenerManager<RoleDeleteListener> addRoleDeleteListener(RoleDeleteListener listener) {
        return addListener(RoleDeleteListener.class, listener);
    }

    @Override
    public List<RoleDeleteListener> getRoleDeleteListeners() {
        return getListeners(RoleDeleteListener.class);
    }

    @Override
    public ListenerManager<UserChangeNicknameListener> addUserChangeNicknameListener(
            UserChangeNicknameListener listener) {
        return addListener(UserChangeNicknameListener.class, listener);
    }

    @Override
    public List<UserChangeNicknameListener> getUserChangeNicknameListeners() {
        return getListeners(UserChangeNicknameListener.class);
    }

    @Override
    public ListenerManager<LostConnectionListener> addLostConnectionListener(LostConnectionListener listener) {
        return addListener(LostConnectionListener.class, listener);
    }

    @Override
    public List<LostConnectionListener> getLostConnectionListeners() {
        return getListeners(LostConnectionListener.class);
    }

    @Override
    public ListenerManager<ReconnectListener> addReconnectListener(ReconnectListener listener) {
        return addListener(ReconnectListener.class, listener);
    }

    @Override
    public List<ReconnectListener> getReconnectListeners() {
        return getListeners(ReconnectListener.class);
    }

    @Override
    public ListenerManager<ResumeListener> addResumeListener(ResumeListener listener) {
        return addListener(ResumeListener.class, listener);
    }

    @Override
    public List<ResumeListener> getResumeListeners() {
        return getListeners(ResumeListener.class);
    }

    @Override
    public ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener) {
        return addListener(ServerTextChannelChangeTopicListener.class, listener);
    }

    @Override
    public List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners() {
        return getListeners(ServerTextChannelChangeTopicListener.class);
    }

    @Override
    public ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener) {
        return addListener(UserRoleAddListener.class, listener);
    }

    @Override
    public List<UserRoleAddListener> getUserRoleAddListeners() {
        return getListeners(UserRoleAddListener.class);
    }

    @Override
    public ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener) {
        return addListener(UserRoleRemoveListener.class, listener);
    }

    @Override
    public List<UserRoleRemoveListener> getUserRoleRemoveListeners() {
        return getListeners(UserRoleRemoveListener.class);
    }

    @Override
    public ListenerManager<UserChangeNameListener> addUserChangeNameListener(UserChangeNameListener listener) {
        return addListener(UserChangeNameListener.class, listener);
    }

    @Override
    public List<UserChangeNameListener> getUserChangeNameListeners() {
        return getListeners(UserChangeNameListener.class);
    }

    @Override
    public ListenerManager<UserChangeDiscriminatorListener> addUserChangeDiscriminatorListener(
            UserChangeDiscriminatorListener listener) {
        return addListener(UserChangeDiscriminatorListener.class, listener);
    }

    @Override
    public List<UserChangeDiscriminatorListener> getUserChangeDiscriminatorListeners() {
        return getListeners(UserChangeDiscriminatorListener.class);
    }

    @Override
    public ListenerManager<UserChangeAvatarListener> addUserChangeAvatarListener(UserChangeAvatarListener listener) {
        return addListener(UserChangeAvatarListener.class, listener);
    }

    @Override
    public List<UserChangeAvatarListener> getUserChangeAvatarListeners() {
        return getListeners(UserChangeAvatarListener.class);
    }

    @Override
    public ListenerManager<WebhooksUpdateListener> addWebhooksUpdateListener(WebhooksUpdateListener listener) {
        return addListener(WebhooksUpdateListener.class, listener);
    }

    @Override
    public List<WebhooksUpdateListener> getWebhooksUpdateListeners() {
        return getListeners(WebhooksUpdateListener.class);
    }

    @Override
    public ListenerManager<ChannelPinsUpdateListener> addChannelPinsUpdateListener(ChannelPinsUpdateListener listener) {
        return addListener(ChannelPinsUpdateListener.class, listener);
    }

    @Override
    public List<ChannelPinsUpdateListener> getChannelPinsUpdateListeners() {
        return getListeners(ChannelPinsUpdateListener.class);
    }

    @Override
    public ListenerManager<CachedMessagePinListener> addCachedMessagePinListener(CachedMessagePinListener listener) {
        return addListener(CachedMessagePinListener.class, listener);
    }

    @Override
    public List<CachedMessagePinListener> getCachedMessagePinListeners() {
        return getListeners(CachedMessagePinListener.class);
    }

    @Override
    public ListenerManager<CachedMessageUnpinListener> addCachedMessageUnpinListener(
            CachedMessageUnpinListener listener) {
        return addListener(CachedMessageUnpinListener.class, listener);
    }

    @Override
    public List<CachedMessageUnpinListener> getCachedMessageUnpinListeners() {
        return getListeners(CachedMessageUnpinListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ListenerManager<? extends GloballyAttachableListener>> addListener(
            GloballyAttachableListener listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(GloballyAttachableListener.class::isAssignableFrom)
                .filter(listenerClass -> listenerClass != GloballyAttachableListener.class)
                .map(listenerClass -> (Class<GloballyAttachableListener>) listenerClass)
                .map(listenerClass -> addListener(listenerClass, listener))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GloballyAttachableListener> ListenerManager<T> addListener(Class<T> listenerClass, T listener) {
        return (ListenerManager<T>) listeners
                .computeIfAbsent(listenerClass, key -> Collections.synchronizedMap(new LinkedHashMap<>()))
                .computeIfAbsent(listener, key -> new ListenerManagerImpl<>(this, listener, listenerClass));
    }

    @Override
    public <T extends GloballyAttachableListener> void removeListener(Class<T> listenerClass, T listener) {
        synchronized (listeners) {
            Map<GloballyAttachableListener, ListenerManagerImpl<? extends GloballyAttachableListener>> classListeners =
                    listeners.get(listenerClass);
            if (classListeners == null) {
                return;
            }
            ListenerManagerImpl<? extends GloballyAttachableListener> listenerManager = classListeners.get(listener);
            if (listenerManager == null) {
                return;
            }
            classListeners.remove(listener);
            listenerManager.removed();
            // Clean it up
            if (classListeners.isEmpty()) {
                listeners.remove(listenerClass);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeListener(GloballyAttachableListener listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(GloballyAttachableListener.class::isAssignableFrom)
                .filter(listenerClass -> listenerClass != GloballyAttachableListener.class)
                .map(listenerClass -> (Class<GloballyAttachableListener>) listenerClass)
                .forEach(listenerClass -> removeListener(listenerClass, listener));
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void finalize() throws Throwable {
        disconnect();
        super.finalize();
    }
}
