import {useForm} from "react-hook-form";
import type {CdDTO, Track} from "../types.tsx";
import {useEffect} from "react";
import {useParams} from "react-router-dom";

type FormValues = {
    title: string,
    performer: string,
    publicationYear: string,
    coverUrl: string | null,
    track1title: string,
    track1time: string,
    track2title: string,
    track2time: string
}
type CdPageProps = {
    onChangePage: (page: string) => void,
    onAddCd: (newCd: CdDTO) => void,
    onError: (message: string) => void
}

export default function AddCdPage(props: Readonly<CdPageProps>) {
    const param= useParams();
    const { register, handleSubmit, reset,
        formState: { errors, isValid },
    } = useForm<FormValues>({ mode: 'onChange' });

    function submit(data: FormValues) {
        const year: number = Number.parseInt(data.publicationYear, 10)
        const t1: Track= {position: 1, trackTitle: data.track1title,
                          time: data.track1time}
        const t2: Track = {position: 2, trackTitle: data.track2title,
                           time: data.track2time}
        const trackList: Track[] = [t1, t2]
        const cd : CdDTO = {
            cdTitle: data.title,
            performer: data.performer,
            publicationYear: year,
            tracks: trackList,
            coverUrl: null,
            cdCollection: {
                id: param.collId!
            }
        }

        props.onAddCd(cd)
        reset({title: '', performer: '', publicationYear: '',
               track1title: '', track1time: '', track2title: '',
               track2time: ''})
    }

    useEffect(() => {
        props.onChangePage("add-cd")
    }, []);
    useEffect(() => {
        if (errors.title) {
            props.onError(errors.title.message!)
        } else if (errors.performer) {
            props.onError(errors.performer.message!)
        } else {
            props.onError("")
        }
    }, [errors, props]);

    return (
        <div className="page">
            <form className="new-cd"
                  onSubmit={handleSubmit(submit)}>
                <label id="lbl-cd-titel">
                    CD-Titel:
                    <input id="txt-cd-titel" type="text"
                           {...register("title", {
                               required: "Der Titel der CD ist erforderlich!"
                           })}
                    />
                </label>
                <label id="lbl-cd-perform">
                    Interpret:
                    <input id="txt-cd-perform" type="text"
                           {...register("performer", {
                               required: "Der Interpret der CD ist erforderlich!"
                           })}
                    />
                </label>
                <label id="lbl-cd-year">
                    Jahr der Veröffentlichung:
                    <input id="txt-cd-year" type="text"
                           {...register("publicationYear")}
                    />
                </label>
                <label id="lbl-cd-style"></label>
                <label id="lbl-cd-storage"></label>
                <label className="tracks">
                    Stücke:
                    <label className="track">
                        1.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text"
                                   {...register("track1title")}
                            />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text"
                                   {...register("track1time")}
                            />
                        </label>
                    </label>
                    <label className="track">
                        2.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text"
                                   {...register("track2title")}
                            />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text"
                                   {...register("track2time")}
                            />
                        </label>
                    </label>
                    <label className="track">
                        3.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text" />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text" />
                        </label>
                    </label>
                    <label className="track">
                        4.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text" />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text" />
                        </label>
                    </label>
                    <label className="track">
                        5.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text" />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text" />
                        </label>
                    </label>
                    <label className="track">
                        6.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text" />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text" />
                        </label>
                    </label>
                    <label className="track">
                        7.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text" />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text" />
                        </label>
                    </label>
                    <label className="track">
                        8.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text" />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text" />
                        </label>
                    </label>
                    <label className="track">
                        9.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text" />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text" />
                        </label>
                    </label>
                    <label className="track">
                        10.
                        <label className="lbl-track-titel">
                            Titel:
                            <input className="txt-track-titel"
                                   type="text" />
                        </label>
                        <label className="lbl-track-time">
                            Zeit:
                            <input className="txt-track-time"
                                   type="text" />
                        </label>
                    </label>
                </label>
                <div className="new-cd-footer">
                    <button id="more-tracks-btn" type={"button"}>
                        Mehr Stücke
                    </button>
                    <button id="add-cd-btn" type="submit"
                            disabled={!isValid}
                            onClick={handleSubmit(submit)}>
                        CD abspeichern
                    </button>
                </div>
            </form>
        </div>
    )
}