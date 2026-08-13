import "./AddCdPage.css";
import {type CD, type CdDTO, Genres, type GenresKey, type Track} from "../types/CdTypes.tsx";
import {useFieldArray, useForm} from "react-hook-form";
import {useEffect} from "react";
import type {CdCollection} from "../types/CollectionTypes.tsx";

type FormValues = {
    title: string,
    performer: string,
    publicationYear: string,
    coverUrl: string,
    genre: GenresKey | "",
    storageLocation: string,
    trackTT: {title: string, time: string}[]
}
type EditCdPageProps = {
    cd: CD,
    coll: CdCollection,
    onChangePage: (page: string) => void,
    onEditCd: (id: string, updatedCd: CdDTO) => void,
    onError: (message: string) => void
}

export default function EditCd(props: Readonly<EditCdPageProps>){
    const { control, register, handleSubmit,
            formState: { errors, isValid }
    } = useForm<FormValues>(
        {
            defaultValues: {
                title: props.cd.cdTitle,
                performer: props.cd.performer,
                publicationYear: props.cd.publicationYear.toString(),
                coverUrl: props.cd.coverUrl,
                genre: props.cd.genres,
                storageLocation: props.cd.storageLocation,
                trackTT: props.cd.tracks.map( track => (
                    {title: track.trackTitle, time: track.time}
                ))
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
            genres: data.genre as GenresKey,
            storageLocation: data.storageLocation,
            tracks: trackList,
            coverUrl: data.coverUrl,
            cdCollection: {
                id: props.coll.id
            }
        }

        props.onEditCd(props.cd.id, cd)
    }

    useEffect(() => {
        props.onChangePage("edit-cd")
    }, [props]);
    useEffect(() => {
        if (errors.title) {
            props.onError(errors.title.message!)
        } else if (errors.performer) {
            props.onError(errors.performer.message!)
        } else if (errors.genre) {
            props.onError(errors.genre.message!)
        } else if (errors.trackTT){
            const timeError = fields
                .map((_, index) => errors.trackTT?.[index]?.time)
                .find(Boolean)
            props.onError(timeError ? timeError.message! : "")
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
                               required: "Der Titel der CD ist erforderlich!",
                               pattern: {
                                   value: /\S/,
                                   message: "Der Titel darf nicht nur Leerzeichen haben!"
                               }
                           })}
                    />
                </label>
                <label id="lbl-cd-perform">
                    Interpret:
                    <input id="txt-cd-perform" type="text"
                           {...register("performer", {
                               required: "Der Interpret der CD ist erforderlich!",
                               pattern: {
                                   value: /\S/,
                                   message: "Der Interpret darf nicht nur Leerzeichen haben!"
                               }
                           })}
                    />
                </label>
                <label id="lbl-cd-year">
                    Jahr der Veröffentlichung:
                    <input id="txt-cd-year" type="text"
                           {...register("publicationYear")}
                    />
                </label>
                <label id="lbl-cd-cover">
                    URL für Cover Bild:
                    <input id="txt-cd-cover" type="text"
                           {...register("coverUrl")}
                    />
                </label>
                <label id="lbl-cd-style">
                    Stil-Richtung:
                    <select id="sel-cd-style"
                            {...register("genre", {
                                required: "Die Stil-Richtung ist erforderlich!"
                            })}>
                        <option value="">nichts ausgewählt</option>
                        {Object.entries(Genres).map(([key, details]) => (
                            <option key={key} value={key}>
                                {details.style}
                            </option>
                        ))}
                    </select>
                </label>
                <label id="lbl-cd-storage">
                    Ablageort:
                    <input id="txt-cd-storage" type="text"
                           {...register("storageLocation")}
                    />
                </label>
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
                                       {...register(`trackTT.${index}.time`, {
                                           pattern: {
                                               value: /[0-5]?\d:[0-5]\d/,
                                               message: "Die Zeit muss das Format mm:ss oder m:ss haben!"
                                           }
                                       })}
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