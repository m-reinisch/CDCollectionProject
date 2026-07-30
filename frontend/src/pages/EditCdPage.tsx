import "./AddCdPage.css";
import type {CD, CdDTO, Track} from "../types/types.tsx";
import {useParams} from "react-router-dom";
import {useFieldArray, useForm} from "react-hook-form";
import {useEffect} from "react";

type FormValues = {
    title: string,
    performer: string,
    publicationYear: string,
    coverUrl: string | null,
    trackTT: {title: string, time: string}[]
}
type EditCdPageProps = {
    cd: CD,
    onChangePage: (page: string) => void,
    onEditCd: (id: string, updatedCd: CdDTO) => void,
    onError: (message: string) => void
}

export default function EditCd(props: Readonly<EditCdPageProps>){
    const param= useParams();
    const { control, register, handleSubmit, reset,
        formState: { errors, isValid }
    } = useForm<FormValues>(
        {
            defaultValues: {
                title: props.cd.cdTitle,
                performer: props.cd.performer,
                publicationYear: props.cd.publicationYear.toString(),
                coverUrl: props.cd.coverUrl,
//                trackTT: [props.cd.tracks.forEach( track => {
//                    {title: track.trackTitle, time: track.time}
//                })]
            },
            mode: 'onChange'
        }
    );
    const { fields, append } = useFieldArray({
        control,
        name: "trackTT"
    });

    function submit(data: FormValues) {
        const year: number = Number.parseInt(data.publicationYear, 10)
        const trackList: Track[]= []
        let ix: number = 1;

        data.trackTT.forEach(tt => {
            if (tt.title) {
                const track: Track = {
                    position: ix,
                    trackTitle: tt.title,
                    time: tt.time
                }
                trackList.push(track)
            }
            ix = ix + 1;
        })
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

        props.onEditCd('0', cd)
    }

    useEffect(() => {
        props.onChangePage("edit-cd")
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

    return(
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
                    {fields.map( (field, index) => (
                        <label className="track" key={field.id}>
                            {index + 1}.
                            <label className="lbl-track-titel">
                                Titel:
                                <input className="txt-track-titel"
                                       type="text"
                                       {...register(`trackTT.${index}.title`)}
                                />
                            </label>
                            <label className="lbl-track-time">
                                Zeit:
                                <input className="txt-track-time"
                                       type="text"
                                       {...register(`trackTT.${index}.time`)}
                                />
                            </label>
                        </label>
                    ))}
                </label>
                <div className="new-cd-footer">
                    <button id="more-tracks-btn" type="button"
                            onClick={ () =>
                                append({title: "", time: ""})}>
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