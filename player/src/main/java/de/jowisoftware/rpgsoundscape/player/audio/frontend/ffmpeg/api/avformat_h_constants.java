// Generated by jextract

package de.jowisoftware.rpgsoundscape.player.audio.frontend.ffmpeg.api;

import jdk.incubator.foreign.FunctionDescriptor;
import jdk.incubator.foreign.GroupLayout;
import jdk.incubator.foreign.MemoryHandles;
import jdk.incubator.foreign.MemoryLayout;
import jdk.incubator.foreign.MemoryLayout.PathElement;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;

import static jdk.incubator.foreign.CLinker.C_CHAR;
import static jdk.incubator.foreign.CLinker.C_FLOAT;
import static jdk.incubator.foreign.CLinker.C_INT;
import static jdk.incubator.foreign.CLinker.C_LONG;
import static jdk.incubator.foreign.CLinker.C_POINTER;

class avformat_h_constants {
    static final FunctionDescriptor avformat_open_input$FUNC_ = FunctionDescriptor.of(C_INT,
            C_POINTER,
            C_POINTER,
            C_POINTER,
            C_POINTER
    );

    static final MethodHandle avformat_open_input$MH_ = RuntimeHelper.downcallHandle(
            "avformat", "avformat_open_input",
            "(Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)I",
            avformat_open_input$FUNC_
    );

    static final GroupLayout AVRational$LAYOUT_ = MemoryLayout.ofStruct(
            C_INT.withName("num"),
            C_INT.withName("den")
    );

    static final MemoryLayout AVStream$struct$LAYOUT_ = MemoryLayout.ofStruct(
            C_INT.withName("index"),
            C_INT.withName("id"),
            C_POINTER.withName("codec"),
            C_POINTER.withName("priv_data"),
            AVRational$LAYOUT_.withName("time_base"),
            C_LONG.withName("start_time"),
            C_LONG.withName("duration"),
            C_LONG.withName("nb_frames"),
            C_INT.withName("disposition"),
            C_INT.withName("discard"),
            MemoryLayout.ofStruct(
                    C_INT.withName("num"),
                    C_INT.withName("den")
            ).withName("sample_aspect_ratio"),
            C_POINTER.withName("metadata"),
            MemoryLayout.ofStruct(
                    C_INT.withName("num"),
                    C_INT.withName("den")
            ).withName("avg_frame_rate"),
            MemoryLayout.ofStruct(
                    C_POINTER.withName("buf"),
                    C_LONG.withName("pts"),
                    C_LONG.withName("dts"),
                    C_POINTER.withName("data"),
                    C_INT.withName("size"),
                    C_INT.withName("stream_index"),
                    C_INT.withName("flags"),
                    MemoryLayout.ofPaddingBits(32),
                    C_POINTER.withName("side_data"),
                    C_INT.withName("side_data_elems"),
                    MemoryLayout.ofPaddingBits(32),
                    C_LONG.withName("duration"),
                    C_LONG.withName("pos"),
                    C_LONG.withName("convergence_duration")
            ).withName("attached_pic"),
            C_POINTER.withName("side_data"),
            C_INT.withName("nb_side_data"),
            C_INT.withName("event_flags"),
            MemoryLayout.ofStruct(
                    C_INT.withName("num"),
                    C_INT.withName("den")
            ).withName("r_frame_rate"),
            C_POINTER.withName("recommended_encoder_configuration"),
            C_POINTER.withName("codecpar"),
            C_POINTER.withName("info"),
            C_INT.withName("pts_wrap_bits"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("first_dts"),
            C_LONG.withName("cur_dts"),
            C_LONG.withName("last_IP_pts"),
            C_INT.withName("last_IP_duration"),
            C_INT.withName("probe_packets"),
            C_INT.withName("codec_info_nb_frames"),
            C_INT.withName("need_parsing"),
            C_POINTER.withName("parser"),
            C_POINTER.withName("last_in_packet_buffer"),
            MemoryLayout.ofStruct(
                    C_POINTER.withName("filename"),
                    C_POINTER.withName("buf"),
                    C_INT.withName("buf_size"),
                    MemoryLayout.ofPaddingBits(32),
                    C_POINTER.withName("mime_type")
            ).withName("probe_data"),
            MemoryLayout.ofSequence(17, C_LONG).withName("pts_buffer"),
            C_POINTER.withName("index_entries"),
            C_INT.withName("nb_index_entries"),
            C_INT.withName("index_entries_allocated_size"),
            C_INT.withName("stream_identifier"),
            C_INT.withName("program_num"),
            C_INT.withName("pmt_version"),
            C_INT.withName("pmt_stream_idx"),
            C_LONG.withName("interleaver_chunk_size"),
            C_LONG.withName("interleaver_chunk_duration"),
            C_INT.withName("request_probe"),
            C_INT.withName("skip_to_keyframe"),
            C_INT.withName("skip_samples"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("start_skip_samples"),
            C_LONG.withName("first_discard_sample"),
            C_LONG.withName("last_discard_sample"),
            C_INT.withName("nb_decoded_frames"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("mux_ts_offset"),
            C_LONG.withName("pts_wrap_reference"),
            C_INT.withName("pts_wrap_behavior"),
            C_INT.withName("update_initial_durations_done"),
            MemoryLayout.ofSequence(17, C_LONG).withName("pts_reorder_error"),
            MemoryLayout.ofSequence(17, C_CHAR).withName("pts_reorder_error_count"),
            MemoryLayout.ofPaddingBits(56),
            C_LONG.withName("last_dts_for_order_check"),
            C_CHAR.withName("dts_ordered"),
            C_CHAR.withName("dts_misordered"),
            MemoryLayout.ofPaddingBits(16),
            C_INT.withName("inject_global_side_data"),
            MemoryLayout.ofStruct(
                    C_INT.withName("num"),
                    C_INT.withName("den")
            ).withName("display_aspect_ratio"),
            C_POINTER.withName("internal")
    ).withName("AVStream");
    static final FunctionDescriptor av_get_default_channel_layout$FUNC_ = FunctionDescriptor.of(C_LONG,
            C_INT
    );
    static final MethodHandle av_get_default_channel_layout$MH_ = RuntimeHelper.downcallHandle(
            "avformat", "av_get_default_channel_layout",
            "(I)J",
            av_get_default_channel_layout$FUNC_
    );
    static final MemoryLayout AVFrame$struct$LAYOUT_ = MemoryLayout.ofStruct(
            MemoryLayout.ofSequence(8, C_POINTER).withName("data"),
            MemoryLayout.ofSequence(8, C_INT).withName("linesize"),
            C_POINTER.withName("extended_data"),
            C_INT.withName("width"),
            C_INT.withName("height"),
            C_INT.withName("nb_samples"),
            C_INT.withName("format"),
            C_INT.withName("key_frame"),
            C_INT.withName("pict_type"),
            MemoryLayout.ofStruct(
                    C_INT.withName("num"),
                    C_INT.withName("den")
            ).withName("sample_aspect_ratio"),
            C_LONG.withName("pts"),
            C_LONG.withName("pkt_pts"),
            C_LONG.withName("pkt_dts"),
            C_INT.withName("coded_picture_number"),
            C_INT.withName("display_picture_number"),
            C_INT.withName("quality"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("opaque"),
            MemoryLayout.ofSequence(8, C_LONG).withName("error"),
            C_INT.withName("repeat_pict"),
            C_INT.withName("interlaced_frame"),
            C_INT.withName("top_field_first"),
            C_INT.withName("palette_has_changed"),
            C_LONG.withName("reordered_opaque"),
            C_INT.withName("sample_rate"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("channel_layout"),
            MemoryLayout.ofSequence(8, C_POINTER).withName("buf"),
            C_POINTER.withName("extended_buf"),
            C_INT.withName("nb_extended_buf"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("side_data"),
            C_INT.withName("nb_side_data"),
            C_INT.withName("flags"),
            C_INT.withName("color_range"),
            C_INT.withName("color_primaries"),
            C_INT.withName("color_trc"),
            C_INT.withName("colorspace"),
            C_INT.withName("chroma_location"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("best_effort_timestamp"),
            C_LONG.withName("pkt_pos"),
            C_LONG.withName("pkt_duration"),
            C_POINTER.withName("metadata"),
            C_INT.withName("decode_error_flags"),
            C_INT.withName("channels"),
            C_INT.withName("pkt_size"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("qscale_table"),
            C_INT.withName("qstride"),
            C_INT.withName("qscale_type"),
            C_POINTER.withName("qp_table_buf"),
            C_POINTER.withName("hw_frames_ctx"),
            C_POINTER.withName("opaque_ref"),
            C_LONG.withName("crop_top"),
            C_LONG.withName("crop_bottom"),
            C_LONG.withName("crop_left"),
            C_LONG.withName("crop_right"),
            C_POINTER.withName("private_ref")
    ).withName("AVFrame");
    static final FunctionDescriptor av_frame_alloc$FUNC_ = FunctionDescriptor.of(C_POINTER);
    static final MethodHandle av_frame_alloc$MH_ = RuntimeHelper.downcallHandle(
            "avformat", "av_frame_alloc",
            "()Ljdk/incubator/foreign/MemoryAddress;",
            av_frame_alloc$FUNC_
    );
    static final MemoryLayout AVPacket$struct$LAYOUT_ = MemoryLayout.ofStruct(
            C_POINTER.withName("buf"),
            C_LONG.withName("pts"),
            C_LONG.withName("dts"),
            C_POINTER.withName("data"),
            C_INT.withName("size"),
            C_INT.withName("stream_index"),
            C_INT.withName("flags"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("side_data"),
            C_INT.withName("side_data_elems"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("duration"),
            C_LONG.withName("pos"),
            C_LONG.withName("convergence_duration")
    ).withName("AVPacket");
    static final VarHandle AVPacket$data$VH_ = MemoryHandles.asAddressVarHandle(AVPacket$struct$LAYOUT_.varHandle(long.class, MemoryLayout.PathElement.groupElement("data")));
    static final VarHandle AVPacket$size$VH_ = AVPacket$struct$LAYOUT_.varHandle(int.class, MemoryLayout.PathElement.groupElement("size"));
    static final VarHandle AVPacket$stream_index$VH_ = AVPacket$struct$LAYOUT_.varHandle(int.class, MemoryLayout.PathElement.groupElement("stream_index"));
    static final FunctionDescriptor av_packet_unref$FUNC_ = FunctionDescriptor.ofVoid(
            C_POINTER
    );
    static final MethodHandle av_packet_unref$MH_ = RuntimeHelper.downcallHandle(
            "avformat", "av_packet_unref",
            "(Ljdk/incubator/foreign/MemoryAddress;)V",
            av_packet_unref$FUNC_
    );
    static final MemoryLayout AVCodec$struct$LAYOUT_ = MemoryLayout.ofStruct(
            C_POINTER.withName("name"),
            C_POINTER.withName("long_name"),
            C_INT.withName("type"),
            C_INT.withName("id"),
            C_INT.withName("capabilities"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("supported_framerates"),
            C_POINTER.withName("pix_fmts"),
            C_POINTER.withName("supported_samplerates"),
            C_POINTER.withName("sample_fmts"),
            C_POINTER.withName("channel_layouts"),
            C_CHAR.withName("max_lowres"),
            MemoryLayout.ofPaddingBits(56),
            C_POINTER.withName("priv_class"),
            C_POINTER.withName("profiles"),
            C_POINTER.withName("wrapper_name"),
            C_INT.withName("priv_data_size"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("next"),
            C_POINTER.withName("update_thread_context"),
            C_POINTER.withName("defaults"),
            C_POINTER.withName("init_static_data"),
            C_POINTER.withName("init"),
            C_POINTER.withName("encode_sub"),
            C_POINTER.withName("encode2"),
            C_POINTER.withName("decode"),
            C_POINTER.withName("close"),
            C_POINTER.withName("send_frame"),
            C_POINTER.withName("receive_packet"),
            C_POINTER.withName("receive_frame"),
            C_POINTER.withName("flush"),
            C_INT.withName("caps_internal"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("bsfs"),
            C_POINTER.withName("hw_configs"),
            C_POINTER.withName("codec_tags")
    ).withName("AVCodec");
    static final MemoryLayout AVCodecContext$struct$LAYOUT_ = MemoryLayout.ofStruct(
            C_POINTER.withName("av_class"),
            C_INT.withName("log_level_offset"),
            C_INT.withName("codec_type"),
            C_POINTER.withName("codec"),
            C_INT.withName("codec_id"),
            C_INT.withName("codec_tag"),
            C_POINTER.withName("priv_data"),
            C_POINTER.withName("internal"),
            C_POINTER.withName("opaque"),
            C_LONG.withName("bit_rate"),
            C_INT.withName("bit_rate_tolerance"),
            C_INT.withName("global_quality"),
            C_INT.withName("compression_level"),
            C_INT.withName("flags"),
            C_INT.withName("flags2"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("extradata"),
            C_INT.withName("extradata_size"),
            MemoryLayout.ofStruct(
                    C_INT.withName("num"),
                    C_INT.withName("den")
            ).withName("time_base"),
            C_INT.withName("ticks_per_frame"),
            C_INT.withName("delay"),
            C_INT.withName("width"),
            C_INT.withName("height"),
            C_INT.withName("coded_width"),
            C_INT.withName("coded_height"),
            C_INT.withName("gop_size"),
            C_INT.withName("pix_fmt"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("draw_horiz_band"),
            C_POINTER.withName("get_format"),
            C_INT.withName("max_b_frames"),
            C_FLOAT.withName("b_quant_factor"),
            C_INT.withName("b_frame_strategy"),
            C_FLOAT.withName("b_quant_offset"),
            C_INT.withName("has_b_frames"),
            C_INT.withName("mpeg_quant"),
            C_FLOAT.withName("i_quant_factor"),
            C_FLOAT.withName("i_quant_offset"),
            C_FLOAT.withName("lumi_masking"),
            C_FLOAT.withName("temporal_cplx_masking"),
            C_FLOAT.withName("spatial_cplx_masking"),
            C_FLOAT.withName("p_masking"),
            C_FLOAT.withName("dark_masking"),
            C_INT.withName("slice_count"),
            C_INT.withName("prediction_method"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("slice_offset"),
            MemoryLayout.ofStruct(
                    C_INT.withName("num"),
                    C_INT.withName("den")
            ).withName("sample_aspect_ratio"),
            C_INT.withName("me_cmp"),
            C_INT.withName("me_sub_cmp"),
            C_INT.withName("mb_cmp"),
            C_INT.withName("ildct_cmp"),
            C_INT.withName("dia_size"),
            C_INT.withName("last_predictor_count"),
            C_INT.withName("pre_me"),
            C_INT.withName("me_pre_cmp"),
            C_INT.withName("pre_dia_size"),
            C_INT.withName("me_subpel_quality"),
            C_INT.withName("me_range"),
            C_INT.withName("slice_flags"),
            C_INT.withName("mb_decision"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("intra_matrix"),
            C_POINTER.withName("inter_matrix"),
            C_INT.withName("scenechange_threshold"),
            C_INT.withName("noise_reduction"),
            C_INT.withName("intra_dc_precision"),
            C_INT.withName("skip_top"),
            C_INT.withName("skip_bottom"),
            C_INT.withName("mb_lmin"),
            C_INT.withName("mb_lmax"),
            C_INT.withName("me_penalty_compensation"),
            C_INT.withName("bidir_refine"),
            C_INT.withName("brd_scale"),
            C_INT.withName("keyint_min"),
            C_INT.withName("refs"),
            C_INT.withName("chromaoffset"),
            C_INT.withName("mv0_threshold"),
            C_INT.withName("b_sensitivity"),
            C_INT.withName("color_primaries"),
            C_INT.withName("color_trc"),
            C_INT.withName("colorspace"),
            C_INT.withName("color_range"),
            C_INT.withName("chroma_sample_location"),
            C_INT.withName("slices"),
            C_INT.withName("field_order"),
            C_INT.withName("sample_rate"),
            C_INT.withName("channels"),
            C_INT.withName("sample_fmt"),
            C_INT.withName("frame_size"),
            C_INT.withName("frame_number"),
            C_INT.withName("block_align"),
            C_INT.withName("cutoff"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("channel_layout"),
            C_LONG.withName("request_channel_layout"),
            C_INT.withName("audio_service_type"),
            C_INT.withName("request_sample_fmt"),
            C_POINTER.withName("get_buffer2"),
            C_INT.withName("refcounted_frames"),
            C_FLOAT.withName("qcompress"),
            C_FLOAT.withName("qblur"),
            C_INT.withName("qmin"),
            C_INT.withName("qmax"),
            C_INT.withName("max_qdiff"),
            C_INT.withName("rc_buffer_size"),
            C_INT.withName("rc_override_count"),
            C_POINTER.withName("rc_override"),
            C_LONG.withName("rc_max_rate"),
            C_LONG.withName("rc_min_rate"),
            C_FLOAT.withName("rc_max_available_vbv_use"),
            C_FLOAT.withName("rc_min_vbv_overflow_use"),
            C_INT.withName("rc_initial_buffer_occupancy"),
            C_INT.withName("coder_type"),
            C_INT.withName("context_model"),
            C_INT.withName("frame_skip_threshold"),
            C_INT.withName("frame_skip_factor"),
            C_INT.withName("frame_skip_exp"),
            C_INT.withName("frame_skip_cmp"),
            C_INT.withName("trellis"),
            C_INT.withName("min_prediction_order"),
            C_INT.withName("max_prediction_order"),
            C_LONG.withName("timecode_frame_start"),
            C_POINTER.withName("rtp_callback"),
            C_INT.withName("rtp_payload_size"),
            C_INT.withName("mv_bits"),
            C_INT.withName("header_bits"),
            C_INT.withName("i_tex_bits"),
            C_INT.withName("p_tex_bits"),
            C_INT.withName("i_count"),
            C_INT.withName("p_count"),
            C_INT.withName("skip_count"),
            C_INT.withName("misc_bits"),
            C_INT.withName("frame_bits"),
            C_POINTER.withName("stats_out"),
            C_POINTER.withName("stats_in"),
            C_INT.withName("workaround_bugs"),
            C_INT.withName("strict_std_compliance"),
            C_INT.withName("error_concealment"),
            C_INT.withName("debug"),
            C_INT.withName("err_recognition"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("reordered_opaque"),
            C_POINTER.withName("hwaccel"),
            C_POINTER.withName("hwaccel_context"),
            MemoryLayout.ofSequence(8, C_LONG).withName("error"),
            C_INT.withName("dct_algo"),
            C_INT.withName("idct_algo"),
            C_INT.withName("bits_per_coded_sample"),
            C_INT.withName("bits_per_raw_sample"),
            C_INT.withName("lowres"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("coded_frame"),
            C_INT.withName("thread_count"),
            C_INT.withName("thread_type"),
            C_INT.withName("active_thread_type"),
            C_INT.withName("thread_safe_callbacks"),
            C_POINTER.withName("execute"),
            C_POINTER.withName("execute2"),
            C_INT.withName("nsse_weight"),
            C_INT.withName("profile"),
            C_INT.withName("level"),
            C_INT.withName("skip_loop_filter"),
            C_INT.withName("skip_idct"),
            C_INT.withName("skip_frame"),
            C_POINTER.withName("subtitle_header"),
            C_INT.withName("subtitle_header_size"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("vbv_delay"),
            C_INT.withName("side_data_only_packets"),
            C_INT.withName("initial_padding"),
            MemoryLayout.ofStruct(
                    C_INT.withName("num"),
                    C_INT.withName("den")
            ).withName("framerate"),
            C_INT.withName("sw_pix_fmt"),
            MemoryLayout.ofStruct(
                    C_INT.withName("num"),
                    C_INT.withName("den")
            ).withName("pkt_timebase"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("codec_descriptor"),
            C_LONG.withName("pts_correction_num_faulty_pts"),
            C_LONG.withName("pts_correction_num_faulty_dts"),
            C_LONG.withName("pts_correction_last_pts"),
            C_LONG.withName("pts_correction_last_dts"),
            C_POINTER.withName("sub_charenc"),
            C_INT.withName("sub_charenc_mode"),
            C_INT.withName("skip_alpha"),
            C_INT.withName("seek_preroll"),
            C_INT.withName("debug_mv"),
            C_POINTER.withName("chroma_intra_matrix"),
            C_POINTER.withName("dump_separator"),
            C_POINTER.withName("codec_whitelist"),
            C_INT.withName("properties"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("coded_side_data"),
            C_INT.withName("nb_coded_side_data"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("hw_frames_ctx"),
            C_INT.withName("sub_text_format"),
            C_INT.withName("trailing_padding"),
            C_LONG.withName("max_pixels"),
            C_POINTER.withName("hw_device_ctx"),
            C_INT.withName("hwaccel_flags"),
            C_INT.withName("apply_cropping"),
            C_INT.withName("extra_hw_frames"),
            C_INT.withName("discard_damaged_percentage"),
            C_LONG.withName("max_samples"),
            C_INT.withName("export_side_data"),
            MemoryLayout.ofPaddingBits(32)
    ).withName("AVCodecContext");
    static final VarHandle AVCodecContext$sample_rate$VH_ = AVCodecContext$struct$LAYOUT_.varHandle(int.class, MemoryLayout.PathElement.groupElement("sample_rate"));
    static final VarHandle AVCodecContext$channels$VH_ = AVCodecContext$struct$LAYOUT_.varHandle(int.class, MemoryLayout.PathElement.groupElement("channels"));
    static final VarHandle AVCodecContext$sample_fmt$VH_ = AVCodecContext$struct$LAYOUT_.varHandle(int.class, MemoryLayout.PathElement.groupElement("sample_fmt"));
    static final VarHandle AVCodecContext$channel_layout$VH_ = AVCodecContext$struct$LAYOUT_.varHandle(long.class, MemoryLayout.PathElement.groupElement("channel_layout"));
    static final FunctionDescriptor avcodec_free_context$FUNC_ = FunctionDescriptor.ofVoid(
            C_POINTER
    );
    static final MethodHandle avcodec_free_context$MH_ = RuntimeHelper.downcallHandle(
            "avformat", "avcodec_free_context",
            "(Ljdk/incubator/foreign/MemoryAddress;)V",
            avcodec_free_context$FUNC_
    );

    static final VarHandle AVStream$codecpar$VH_ = MemoryHandles.asAddressVarHandle(AVStream$struct$LAYOUT_.varHandle(long.class, MemoryLayout.PathElement.groupElement("codecpar")));
    static final MemoryLayout AVFormatContext$struct$LAYOUT_ = MemoryLayout.ofStruct(
            C_POINTER.withName("av_class"),
            C_POINTER.withName("iformat"),
            C_POINTER.withName("oformat"),
            C_POINTER.withName("priv_data"),
            C_POINTER.withName("pb"),
            C_INT.withName("ctx_flags"),
            C_INT.withName("nb_streams"),
            C_POINTER.withName("streams"),
            MemoryLayout.ofSequence(1024, C_CHAR).withName("filename"),
            C_POINTER.withName("url"),
            C_LONG.withName("start_time"),
            C_LONG.withName("duration"),
            C_LONG.withName("bit_rate"),
            C_INT.withName("packet_size"),
            C_INT.withName("max_delay"),
            C_INT.withName("flags"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("probesize"),
            C_LONG.withName("max_analyze_duration"),
            C_POINTER.withName("key"),
            C_INT.withName("keylen"),
            C_INT.withName("nb_programs"),
            C_POINTER.withName("programs"),
            C_INT.withName("video_codec_id"),
            C_INT.withName("audio_codec_id"),
            C_INT.withName("subtitle_codec_id"),
            C_INT.withName("max_index_size"),
            C_INT.withName("max_picture_buffer"),
            C_INT.withName("nb_chapters"),
            C_POINTER.withName("chapters"),
            C_POINTER.withName("metadata"),
            C_LONG.withName("start_time_realtime"),
            C_INT.withName("fps_probe_size"),
            C_INT.withName("error_recognition"),
            MemoryLayout.ofStruct(
                    C_POINTER.withName("callback"),
                    C_POINTER.withName("opaque")
            ).withName("interrupt_callback"),
            C_INT.withName("debug"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("max_interleave_delta"),
            C_INT.withName("strict_std_compliance"),
            C_INT.withName("event_flags"),
            C_INT.withName("max_ts_probe"),
            C_INT.withName("avoid_negative_ts"),
            C_INT.withName("ts_id"),
            C_INT.withName("audio_preload"),
            C_INT.withName("max_chunk_duration"),
            C_INT.withName("max_chunk_size"),
            C_INT.withName("use_wallclock_as_timestamps"),
            C_INT.withName("avio_flags"),
            C_INT.withName("duration_estimation_method"),
            MemoryLayout.ofPaddingBits(32),
            C_LONG.withName("skip_initial_bytes"),
            C_INT.withName("correct_ts_overflow"),
            C_INT.withName("seek2any"),
            C_INT.withName("flush_packets"),
            C_INT.withName("probe_score"),
            C_INT.withName("format_probesize"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("codec_whitelist"),
            C_POINTER.withName("format_whitelist"),
            C_POINTER.withName("internal"),
            C_INT.withName("io_repositioned"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("video_codec"),
            C_POINTER.withName("audio_codec"),
            C_POINTER.withName("subtitle_codec"),
            C_POINTER.withName("data_codec"),
            C_INT.withName("metadata_header_padding"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("opaque"),
            C_POINTER.withName("control_message_cb"),
            C_LONG.withName("output_ts_offset"),
            C_POINTER.withName("dump_separator"),
            C_INT.withName("data_codec_id"),
            MemoryLayout.ofPaddingBits(32),
            C_POINTER.withName("open_cb"),
            C_POINTER.withName("protocol_whitelist"),
            C_POINTER.withName("io_open"),
            C_POINTER.withName("io_close"),
            C_POINTER.withName("protocol_blacklist"),
            C_INT.withName("max_streams"),
            C_INT.withName("skip_estimate_duration_from_pts"),
            C_INT.withName("max_probe_packets"),
            MemoryLayout.ofPaddingBits(32)
    ).withName("AVFormatContext");

    static MethodHandle avformat_open_input$MH() {
        return avformat_open_input$MH_;
    }

    static final FunctionDescriptor avformat_find_stream_info$FUNC_ = FunctionDescriptor.of(C_INT,
            C_POINTER,
            C_POINTER
    );

    static final MethodHandle avformat_find_stream_info$MH_ = RuntimeHelper.downcallHandle(
            "avformat", "avformat_find_stream_info",
            "(Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)I",
            avformat_find_stream_info$FUNC_
    );

    static MethodHandle avformat_find_stream_info$MH() {
        return avformat_find_stream_info$MH_;
    }

    static final FunctionDescriptor av_find_best_stream$FUNC_ = FunctionDescriptor.of(C_INT,
            C_POINTER,
            C_INT,
            C_INT,
            C_INT,
            C_POINTER,
            C_INT
    );

    static final MethodHandle av_find_best_stream$MH_ = RuntimeHelper.downcallHandle(
            "avformat", "av_find_best_stream",
            "(Ljdk/incubator/foreign/MemoryAddress;IIILjdk/incubator/foreign/MemoryAddress;I)I",
            av_find_best_stream$FUNC_
    );

    static MethodHandle av_find_best_stream$MH() {
        return av_find_best_stream$MH_;
    }

    static final FunctionDescriptor av_read_frame$FUNC_ = FunctionDescriptor.of(C_INT,
            C_POINTER,
            C_POINTER
    );

    static final MethodHandle av_read_frame$MH_ = RuntimeHelper.downcallHandle(
            "avformat", "av_read_frame",
            "(Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)I",
            av_read_frame$FUNC_
    );

    static MethodHandle av_read_frame$MH() {
        return av_read_frame$MH_;
    }

    static final FunctionDescriptor avformat_close_input$FUNC_ = FunctionDescriptor.ofVoid(
            C_POINTER
    );

    static final MethodHandle avformat_close_input$MH_ = RuntimeHelper.downcallHandle(
            "avformat", "avformat_close_input",
            "(Ljdk/incubator/foreign/MemoryAddress;)V",
            avformat_close_input$FUNC_
    );

    static int AV_SAMPLE_FMT_S16() {
        return (int) 1L;
    }

    static int AVMEDIA_TYPE_AUDIO() {
        return (int) 1L;
    }

    static final long AVStream$struct$timeBase$offset = AVStream$struct$LAYOUT_.byteOffset(
            PathElement.groupElement("time_base"));

    static final long AVStream$struct$timeBase$size = AVRational$LAYOUT_.byteSize();
}

